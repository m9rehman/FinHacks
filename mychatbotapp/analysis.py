import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
from pandas import DataFrame

fInfo = pd.read_csv('banking2.csv')



# Converter function: YYYY-MM-DD to a timestamp
datefunc = lambda x: mdates.date2num(datetime.strptime(x, '%Y-%m-%d'))

fInfo["Timestamp"] = fInfo["Date"].apply(datefunc)

# Total Spent
fInfo = fInfo.sort('Timestamp')
# print(fInfo)

startingBalance = 60000

def costSubtract(x):
	global startingBalance
	amount = startingBalance - x
	startingBalance = amount
	return amount

# Creating a remaining Balance series	
fInfo["AmountRemaining"] = fInfo["Spent"].apply(costSubtract)

# print(fInfo["AmountRemaining"])

# print(pd.value_counts(fInfo["Locaticon"])[:])

sum = 0
def sumLocation(x):
	global sum
	global fInfo
	return fInfo["Spent"][fInfo["Location"]== x].sum()
	


uniqueLocs = fInfo["Location"].unique()



fInfoAgg = DataFrame(columns=['Location'], data = uniqueLocs)
fInfoAgg["Totalcost"] = fInfoAgg["Location"].apply(sumLocation)
# print(pd.value_counts(fInfo["Location"]))

fInfoAgg.to_csv('AggregateReport')
fInfo.to_csv('FinalRawReport.csv')
pd.value_counts(fInfo["Location"]).to_csv('FrequencyReport.csv')

# print(fInfoAgg)


timeList = fInfo["Timestamp"].tolist()
balanceList = fInfo["AmountRemaining"].tolist()


# plt.plot(timeList,balanceList)
# plt.show()



