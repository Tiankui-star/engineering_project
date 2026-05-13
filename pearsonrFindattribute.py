import pandas as pd
import glob
import os


data_folder = 'D:\\industrial\\采集原始数据\\processData'
y_file = 'D:\\industrial\\采集原始数据\\processData\\机组实际负荷 366.csv'
out_path = 'D:\\industrial\\采集原始数据\\correlation_analysis.xlsx'


def load_csv_data(file_path):
    df = pd.read_csv(file_path)
    df.columns = df.columns.str.strip()

    timestamp_col = [col for col in df.columns if 'timestamp' in col.lower() or 'time' in col.lower()][0]
    value_col = [col for col in df.columns if 'value' in col.lower()][0]

    df = df[[timestamp_col, value_col]]
    df.columns = ['timestamp', 'value']

    return df


y_data = load_csv_data(y_file)
y_data.columns = ['timestamp', 'y_value']
print(f"Y数据点数: {len(y_data)}")
print(f"Y数据时间范围: {y_data['timestamp'].min()} 到 {y_data['timestamp'].max()}")
print(f"Y数据前5行:\n{y_data.head()}")

# 2. 获取所有其他CSV文件
all_csv = glob.glob(os.path.join(data_folder, '*.csv'))
# 排除目标文件
other_files = [f for f in all_csv if os.path.basename(f) != os.path.basename(y_file)]

print(f"\n找到 {len(other_files)} 个待分析文件")

results = []

for file in other_files:
    print(f"\n分析文件: {os.path.basename(file)}")

    # 加载X数据
    x_data = load_csv_data(file)
    x_data.columns = ['timestamp', 'x_value']

    merged = pd.merge(y_data, x_data, on='timestamp', how='inner')

    data_points = len(merged)
    print(f"  对齐数据点: {data_points}")

    if data_points < 3:
        print(f"  警告: 数据点不足3个，无法计算有效相关系数")
        corr = None
        p_value = None
    else:
        corr = merged['y_value'].corr(merged['x_value'])

        try:
            from scipy.stats import pearsonr

            corr, p_value = pearsonr(merged['y_value'], merged['x_value'])
            print(f"  皮尔森相关系数: {corr:.6f}, p值: {p_value:.4e}")
        except:
            p_value = None
            print(f"  皮尔森相关系数: {corr:.6f}")

    results.append({
        '文件名': os.path.basename(file),
        '相关系数': corr,
        'p值': p_value if p_value else 'N/A',
        '对齐数据点数': data_points,
        '缺失Y数据点数': len(y_data) - data_points
    })

results_df = pd.DataFrame(results)
results_df = results_df.dropna(subset=['相关系数'])
results_df = results_df.sort_values('相关系数', ascending=False)

print("\n" + "=" * 80)
print("相关性分析结果（按相关系数从高到低排序）")
print("=" * 80)
print(results_df[['文件名', '相关系数', 'p值', '对齐数据点数']].to_string(index=False))

print("\n" + "=" * 80)
print("TOP 5 最相关的属性")
print("=" * 80)

top5 = results_df.head(5)
for idx, (_, row) in enumerate(top5.iterrows(), 1):
    print(f"\n{idx}. 文件: {row['文件名']}")
    print(f"   相关系数: {row['相关系数']:.6f}")
    print(f"   p值: {row['p值']}")
    print(f"   对齐数据点数: {row['对齐数据点数']}")

high_corr = results_df[results_df['相关系数'].abs() > 0.7]
print(f"\n强相关性文件（|r| > 0.7）: {len(high_corr)} 个")
if len(high_corr) > 0:
    print(high_corr[['文件名', '相关系数']].to_string(index=False))
high_corr_output = 'D:\\industrial\\采集原始数据\\high_correlation_files.xlsx'
high_corr[['文件名', '相关系数']].to_excel(high_corr_output, index=False)

