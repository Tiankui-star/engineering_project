import numpy as np
import pandas as pd
import glob
import os

# 获取所有CSV文件
csv_files = glob.glob('D:\\industrial\\采集原始数据\\采集数据\\*.csv')
out_path='D:\\industrial\\采集原始数据\\处理后的数据'
for file in csv_files:

    df = pd.read_csv(file)

    df[' Timestamp'] = pd.to_datetime(df[' Timestamp'], unit='s')

    df = df.sort_values(' Timestamp')

    df[' Value'] = df[' Value'].replace(0, np.nan)
    df[' Value'] = df[' Value'].interpolate(method='linear', limit_direction='both')

    df[' Value'] = df[' Value'].ffill().bfill()

    output_path = os.path.join(out_path, os.path.basename(file))

    df.to_csv(output_path, index=False)