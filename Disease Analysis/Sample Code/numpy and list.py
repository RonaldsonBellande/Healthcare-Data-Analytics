#!/usr/bin/env python
# coding: utf-8

# # Numpy basics

import numpy as np
a = np.arange(15).reshape(3,5)

print(a)

print(a.ndim)

print(a.shape)

print(a.size)
print(a.dtype)


print(type(a))


b = np.array([1.0, 2.0, 3.0, 4.0])
print(b)
print(b.dtype)

array = [1,2,3,4,5,6]
np_array = np.array(array)
print(np_array)

c = np.zeros((10,10), dtype = np.int64)
print(c)


#linspace
temp = np.linspace(0,1,11, endpoint=True)
print(temp)

temp = np.linspace(0,20, 50, endpoint=False)
print(temp)

#logspace
temp = np.logspace(-1, 2, 15, endpoint=False, base=10)
print(temp)

help(np.array)

temp = np.fromfunction(lambda i, j: i + j, (3,3), dtype=int)
print(temp)

def equation(x,y):
    return 15*x+y
temp = np.fromfunction(equation,(5,4), dtype=int)
print(temp)

# Operations

temp = np.arange(12).reshape(3,4)
print(temp)

print(temp.sum(axis=0))

print(temp.sum(axis=1))

print(temp.min(),temp.max())

temp1 = a*np.pi/12
print(temp1)

temp2 = np.cos(temp1)
print(temp2)

temp3 = temp1 + temp2
print(temp3)

# Slicing

temp = np.fromfunction(equation, (5,4), dtype=int)
print(temp)


print(temp[1,1])

print(temp[0:2,0:2])

print(temp[:,1])

print(temp.flatten())

for row in temp:
    print(row)

for temp in temp.flat:
    print(temp)

temp = np.arange(10)
print(temp)

temp.shape = (5,2)
print(temp)
print(temp.shape)

temp2 = np.arange(12)
temp3 = temp2[:]
print(temp3)

temp2.shape = (3,4)
print(temp2)

temp4 = temp3[:]
print(temp4)

print(temp3.flatten())

temp3[1] = -4

print(temp4)

temp5 = temp3[3:8]
print(temp5)

temp5[:] = 0
print(temp5)

print(temp2 is temp3)
print(temp3.base is temp3)

print(temp4)
temp3 = temp2.copy()

print(temp3)

temp3[:,:] = 1
print(temp3)

temp = np.arange(25).reshape(5,5)
print(temp)

temp[temp > 10] = 0
print(temp)

temp[temp == 0] = 1
print(temp)

temp = np.arange(12).reshape(3,4)
temp[np.logical_and(temp > 2, temp<= 5)] = 0.0

print(temp)
print(temp > 5)
np_temp = np.array(temp)
print(np_temp)
print(temp is np_temp)
print(np_temp.size)
print(temp.size)

print(np_temp > 5)
print(np_array.dtype)


