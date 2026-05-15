import pandas as pd
import glob
import os

csv_files=glob.glob('D:\\industrial\\采集原始数据\\processData\\*.csv')
out_path='D:\\industrial\\采集原始数据\\deletetime'

for file in csv_files:
    df=pd.read_csv(file)
    df['Timestamp']=pd.to_datetime(df['Timestamp'])
    start = pd.to_datetime('2021/07/27 23:51:00')
    end = pd.to_datetime('2021/08/04 22:14:00')

    # 删除范围内的数据
    df_filtered = df[~df['Timestamp'].between(start, end)]

    filename = os.path.basename(file)
    out_file = os.path.join(out_path, filename)
    df_filtered.to_csv(out_file, index=False)
   # print(f"已处理并保存: {filename}")