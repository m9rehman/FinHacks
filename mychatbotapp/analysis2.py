import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
from pandas import DataFrame

fRInfo = pd.read_csv('FinalRawReport.csv')

timeList = fRInfo["Timestamp"].tolist()
balanceList = fRInfo["AmountRemaining"].tolist()

d = {'Timestamp': timeList,'Balance':balanceList}

newDataFrame = pd.DataFrame(data=d)
print(newDataFrame)

newDataFrame.to_csv('graphReady.csv')

