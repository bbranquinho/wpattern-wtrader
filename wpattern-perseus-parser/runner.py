#!/usr/bin/python

import ConfigParser
import sys
import subprocess
from pprint import pprint

CONFIG_LOC = 'poupart.cfg'

def make_run_string(type, config=None):
    if not config:
        config = ConfigParser.ConfigParser()
        config.read(CONFIG_LOC)

    location = config.get(type, 'location')
    name = config.get(type, 'name')
    server = config.get(type, 'server')
    port = config.get(type, 'port')

    return './run rddl.competition.Client {0} {1} {2} rddl.policy.SPerseusSPUDDPolicy {3} 123456 '.format(location, server, name, port)

def _system_call(call_string):
    print call_string
    callargs = call_string.split()
    retcode = subprocess.call(callargs)

def run(problem_name, type):
    call_string = make_run_string(type) + problem_name
    _system_call(call_string)

if __name__ == '__main__':
    problem_path = sys.argv[1]
    type = 'testing'
    if len(sys.argv) > 2 and sys.argv[2] == 'prod':
        type = 'prod'
    filename = problem_path.split('/')[-1]
    assert filename.endswith('.sperseus')
    problem_name = filename.split('.')[0]
    run(problem_name, type)


