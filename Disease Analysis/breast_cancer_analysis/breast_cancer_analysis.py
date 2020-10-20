#!/usr/bin/env python
# coding: utf-8

# # Breast Cancer Analysis with Machine Learning Model

# In[234]:

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

import math
from sklearn.linear_model import LogisticRegression
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn import metrics
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import confusion_matrix
from sklearn.metrics import precision_score
from sklearn.svm import SVC


cancer_dataset = pd.read_csv('breast_cancer_data.csv')
cancer_dataset.head()

X_axis = cancer_dataset.iloc[:, 1:31].values
Y_axis = cancer_dataset.iloc[:, 31].values
print("Dimensions : {}".format(cancer_dataset.shape))

cancer_dataset.groupby('diagnosis').size()

cancer_dataset.groupby('diagnosis').hist(figsize=(12, 12))

cancer_dataset.isnull().sum()

cancer_dataset.head()

dataframe = pd.DataFrame(Y_axis)

labelencoder_Y_axis = LabelEncoder()
Y_axis = labelencoder_Y_axis.fit_transform(Y_axis)

# Spliting the dataset into the Training set and the Test set
X_train, X_test, Y_train, Y_test = train_test_split(X_axis, Y_axis, test_size = 0.20, random_state = 0)

#Feature Scaling
sc = StandardScaler()
#Split it so you can train and test from diffrent sets
X_train = sc.fit_transform(X_train)
X_test = sc.transform(X_test)

#Fitting the logistic Regression Algorithm to the Training set
classifier = LogisticRegression(random_state = 0)
classifier.fit(X_train, Y_train)

#Predicte the test set results
Y_pred = classifier.predict(X_test)

#Create the confusion matrix
cm = confusion_matrix(Y_test, Y_pred)
print("Accuracy of Logistic Regression:", accuracy_score(Y_test, Y_pred))
print("Precision of Logistic Regression:", precision_score(Y_test, Y_pred, average='weighted'))

#Fitting the Support Vector Machines Algorithm to the Training set
classifier = SVC(kernel = 'linear', random_state = 0)
classifier.fit(X_train, Y_train)

#Predict the test result
Y_pred = classifier.predict(X_test)

#Create a confusion matrix
cm = confusion_matrix(Y_test, Y_pred)
print("Accuracy of SVC:", accuracy_score(Y_test, Y_pred))
print("Precision of SVC:", precision_score(Y_test, Y_pred, average='weighted'))
X_train2, X_test2, Y_train2, Y_test2 = train_test_split(X_axis, Y_axis, test_size = 0.20, random_state = 0)

#Feature Scaling
sc = StandardScaler()

#Split it so you can train and test from diffrent sets, this is where the switch happeneds
X_train2 = sc.fit_transform(X_test2)
X_test2 = sc.transform(X_train2)

classifier2 = LogisticRegression(random_state = 0)
classifier2.fit(X_train2, Y_test2)

#predicting the Test set results
Y_pred2 = classifier2.predict(X_test2)
cm = confusion_matrix(Y_test2, Y_pred2)
print("Accuracy of reverse Logistic Regression:", accuracy_score(Y_test2, Y_pred2))
print("Precision of reverse Logistic Regression:", precision_score(Y_test2, Y_pred2, average='weighted'))

#Fitting the Support Vector Machines Algorithm to the Training set
classifier2 = SVC(kernel = 'linear', random_state = 0)
classifier2.fit(X_train2, Y_test2)

#predict the test ser results
Y_pred2 = classifier2.predict(X_test2)
cm = confusion_matrix(Y_test2, Y_pred2)
print("Accuracy of reverse SVC:",accuracy_score(Y_test2,Y_pred2))
print("Precision of reverse SVC:", precision_score(Y_test2, Y_pred2, average='weighted'))

