import pandas as pd
import glob
import os

csv_files=glob.glob('D:\\industrial\\采集原始数据\\处理后的数据\\*.csv')
out_path='D:\\industrial\\采集原始数据\\processData'
for file in csv_files:
    df=pd.read_csv(file)
    df[' Timestamp']=pd.to_datetime(df[' Timestamp'])
    df['minute']=df[' Timestamp'].dt.floor('min')

    res=df.groupby('minute')[' Value'].mean().reset_index()
    res['Timestamp'] = res['minute'].dt.strftime('%Y/%m/%d %H:%M')
    fin=res[['Timestamp',' Value']]
    base_name = os.path.basename(file)
    name_without_ext = os.path.splitext(base_name)[0]
    out_file = os.path.join(out_path, f'{name_without_ext}.csv')
    fin.to_csv(out_file, index=False)