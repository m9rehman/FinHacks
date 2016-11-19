import pandas as pd
import numpy as np

fInfo = pd.read_csv('banking.csv')

# Julian's starting Balance as of July 1st
startingBalance = 6500
def costSubtract(x):
	global startingBalance
	amount = startingBalance - x
	startingBalance = amount
	return amount

# Creating a remaining Balance series	
fInfo["AmountRemaining"] = fInfo["Spent"].apply(costSubtract)

# Total Spent
# print(fInfo["Spent"].sum())
# print(fInfo["AmountRemaining"])

print(pd.value_counts(fInfo["Location"]))


# fInfo.to_csv('FinalReport.csv')

