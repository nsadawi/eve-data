update: 10/09/2014 by NS

* Folders TS3/ 4,5,6 and 7 contain:
  - Files from the corresponding assays (TS*.csv is the merged version)
  - Averaged version of the merged file
  - Labeled version of the merged file

* I have run WEKA's multiclass NB, kNN (with k = 5) and Decision Trees (j48) on TS[3-7]-Labeled.csv and the results are in Eve-NB-kNN-DTrees.csv
* Percentage split (67% Training & 33 Testing)
* The results are simple confusion matrix based stats and counts


Experiment update: 11/09/2014 by NS
- I have removed autofluorescent, toxic and possibly toxic instances from the labelled datasets
- These are now saved as TS[3-7]-Binary-Labeled.csv
- Bearing in mind that all these assays were done on the same target (DHFR according to notes)
- I have used binary labelled data from TS7 as training data for:
     Decision Tress, Naive Bayes, kNN with k = 5 and SVM
- I have used binary labelled data from TS3-6 as testing data and recorded results in TS[3-6]-TS7.csv files
- The result files are tables showing Chem ID, Actual Class and Predicted Classes made by the above mentioned classifiers
- Observe when the same chem id repeats, this means it is there with diff concentrations!
