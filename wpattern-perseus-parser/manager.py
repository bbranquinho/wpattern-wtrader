#!/usr/bin/python

import ConfigParser
import re
import sys
import time
import logging
from os.path import join
from os import listdir
from optparse import OptionParser

import runner

FILE_ENDING = '.sperseus'
INSTANCE_RE = r'.*__(?P<num>[\d]+)\.sperseus'
TIME_LIMIT = 60*60*7
LOG_FILENAME = 'manager' + str(time.time()) + '.log'
logging.basicConfig(filename=LOG_FILENAME, level=logging.DEBUG)

def _count_param_in_file(filename, param_name):
    file = open(filename)
    are_counting = False
    counted_params = 0
    for line in file:
        if are_counting:
            if line.strip() == ')':
                break  # stop counting
            else:
                counted_params += 1
        else:
            if line.strip() == '(' + param_name:
                are_counting = True  # start counting

    file.close()
    return counted_params

def _count_actions_in_file(filename):
    file = open(filename)
    actions = 0
    for line in file:
        if line.startswith('action'):
            actions += 1
    file.close()
    return actions

def _rank_by_heur(filename):
    vars = _count_param_in_file(filename, 'variables')
    obs = _count_param_in_file(filename, 'observations')
    acts = _count_actions_in_file(filename)
    return (vars + obs, obs, acts)

def ranked_problems(filenames):
    ranked_files = ([(_rank_by_heur(fname), fname) 
                    for fname in filenames])
    for num, fname in sorted(ranked_files):
        problem = fname.split('/')[-1]
        instance_name = problem.split('.')[0]
        yield instance_name
    
if __name__ == '__main__':
    start = time.time()
    parser = OptionParser(usage='usage: %prog [options]')

    parser.add_option('-d', '--dir',
                    action='store',
                    default='files/test_comp/spudd_sperseus',
                    help='directory to look for sperseus files in')
    parser.add_option('-t', '--type',
                    action='store',
                    default='prod',
                    choices=['prod', 'testing'],
                    help='settings type for connection to server')
    options, args = parser.parse_args()
    spers_dir = options.dir
    all_filenames = listdir(spers_dir)
    sp_filenames = ([join(spers_dir, name) 
                    for name in all_filenames 
                    if name.endswith(FILE_ENDING)])
    prev_problem_time = start
    for problem in ranked_problems(sp_filenames):
        logging.info(problem)
        runner.run(problem, options.type)

        finished = time.time()
        logging.info(finished - prev_problem_time)
        prev_problem_time = finished

        if time.time() - start > TIME_LIMIT:
            break
