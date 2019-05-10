#!/bin/bash
mpicc ./cpi.c -o test.out
mpirun test.out
