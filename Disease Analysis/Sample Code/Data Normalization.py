#!/usr/bin/env python
# coding: utf-8

# # Data Normalization

from sklearn import datasets
from sklearn import preprocessing

dataset = datasets.load_breast_cancer()

print(dataset.data.shape)

print(dataset.data)
print(dataset.target)

X_axis = dataset.data
Y_axis = dataset.target

#Normalized
normalized_X_axis = preprocessing.normalize(X_axis)
print(normalized_X_axis)
print(normalized_X_axis.shape)

