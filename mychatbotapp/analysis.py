import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime

# Converter function
datefunc = lambda x: mdates.date2num(datetime.strptime(x, '%Y-%m-%d'))

fInfo = pd.read_csv('banking2.csv')


fInfo["Timestamp"] = fInfo["Date"].apply(datefunc)
# print(fInfo["Timestamp"][530:])



# Julian's starting Balance as of July 1st


# Total Spent
fInfo = fInfo.sort('Timestamp')
print(fInfo)


startingBalance = 60000
def costSubtract(x):
	global startingBalance
	amount = startingBalance - x
	startingBalance = amount
	return amount

# Creating a remaining Balance series	
fInfo["AmountRemaining"] = fInfo["Spent"].apply(costSubtract)

# print(fInfo["AmountRemaining"])

# print(pd.value_counts(fInfo["Location"]))

timeList = fInfo["Timestamp"].tolist()
balanceList = fInfo["AmountRemaining"].tolist()



# print(timeList)


plt.plot(timeList,balanceList)
plt.show()


fInfo.to_csv('FinalReport3.csv')

