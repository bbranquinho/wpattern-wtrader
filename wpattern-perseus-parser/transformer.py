#!/usr/bin/python

import sys
from os import listdir, remove, rename
from os.path import join
import re

FILE_ENDING = '.sperseus'
DISCOUNT = r'discount [\d\.]+'

def change_discount(file, new_discount=0.95):
    fname = file.name
    tmpname = fname + '.tmp'
    new_file = open(tmpname, 'w')
    def writeln(line):
        new_file.write(line + '\n')

    for line in file:
        line = line.rstrip()
        if re.match(DISCOUNT, line):
            writeln('discount ' + str(new_discount))
            print 'changed ' + fname
        else:
            writeln(line)
    new_file.close()
    file.close()
    remove(fname)
    rename(tmpname, fname)

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'Enter modification directory'
    else:
        dir = sys.argv[1]
        all_filenames = listdir(dir)
        sp_filenames = [name for name in all_filenames if name.endswith(FILE_ENDING)]
        for filename in sp_filenames:
            file = open(join(dir, filename))
            change_discount(file)
