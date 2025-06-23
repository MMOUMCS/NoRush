# 작성일자: 2025-06-13
# 최종수정일자 : 2025-06-14
# 작성자 : 심준서

import pandas as pd
from sklearn.preprocessing import LabelEncoder
import lightgbm as lgb
import joblib

def train_and_save_model():

    print("모델 학습시작")

    file_path = './대구교통공사_역별일별시간별승하차인원현황_20250430.csv'
    try:
        df = pd.read_csv(file_path, encoding='utf-8')
    except UnicodeDecodeError:
        df = pd.read_csv(file_path, encoding='cp949')

    time_cols = [f'{h:02d}시-{(h+1):02d}시' for h in range(5, 24)]
    time_cols.append('23시-24시')
    
    df_long = df.melt(id_vars=['월', '일', '역명', '승하차'], value_vars=time_cols, var_name='시간대', value_name='인원')
    
    df_pivot = df_long.pivot_table(index=['월', '일', '역명', '시간대'], columns='승하차', values='인원').reset_index()
    df_pivot.columns.name = None
    df_pivot = df_pivot.rename(columns={'승차': '승차인원', '하차': '하차인원'}).fillna(0)

    print("데이터 전처리 완료. df_pivot 변수 생성")

    df_pivot['작업일자'] = pd.to_datetime('2025-' + df_pivot['월'].astype(str) + '-' + df_pivot['일'].astype(str))
    df_pivot['요일'] = df_pivot['작업일자'].dt.dayofweek
    df_pivot['시간'] = df_pivot['시간대'].str.slice(0, 2).astype(int)

    le = LabelEncoder()
    df_pivot['역명_encoded'] = le.fit_transform(df_pivot['역명'])
    
    print("특성 생성 완료")

    features = ['월', '일', '요일', '시간', '역명_encoded']
    target = '승차인원'
    X, y = df_pivot[features], df_pivot[target]

    lgbm = lgb.LGBMRegressor(random_state=42)
    lgbm.fit(X, y)

    joblib.dump(lgbm, 'lgbm_ridership_model.pkl')
    joblib.dump(le, 'station_label_encoder.pkl')
    
    print("저장 완료")

if __name__ == '__main__':
    train_and_save_model()