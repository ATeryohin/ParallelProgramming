#!/bin/bash
mpicc ./cpi.c -o test.out
mpirun -np 10 test.out
