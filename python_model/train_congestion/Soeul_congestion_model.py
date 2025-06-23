# 작성일자: 2025-06-14
# 최종수정일자 : 2025-06-24
# 작성자 : 김수정
# 서울교통공사 데이터(2025-06-30 ~ 2025-04-14)를 학습한 데이터 (train용)

import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler, OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.multioutput import MultiOutputRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, r2_score

# 1. Load Data
df = pd.read_csv('/content/drive/MyDrive/norush_train_data/서울교통공사_지하철혼잡도정보_(수정값).csv', encoding='utf-8-sig')  # 또는 'euc-kr'

# 2. Column name 정리 (공백 제거 등)
df.columns = df.columns.str.strip()

# 3. 피처 & 타겟 분리
# 특징 변수
feature_cols = ['요일구분', '호선', '역번호', '출발역', '상하구분']

# 타겟 변수: 시간대 컬럼 (5시30분 ~ 00시30분)
time_columns = [col for col in df.columns if ':' in col]

X = df[feature_cols]
Y = df[time_columns]

# 4. 전처리: 범주형 인코딩 + 수치 정규화 (선택)
categorical_features = ['요일구분', '호선', '출발역', '상하구분']
numeric_features = ['역번호']

preprocessor = ColumnTransformer(transformers=[
    ('cat', OneHotEncoder(handle_unknown='ignore'), categorical_features),
    ('num', StandardScaler(), numeric_features)
])

# 5. 모델 구성 (RandomForest + MultiOutput)
model = MultiOutputRegressor(RandomForestRegressor(n_estimators=100, random_state=42))

pipeline = Pipeline(steps=[
    ('preprocessor', preprocessor),
    ('regressor', model)
])

# 6. 학습/테스트 데이터 분리
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=42)


# 7. 모델 학습
pipeline.fit(X_train, Y_train)

# 8. 평가
Y_pred = pipeline.predict(X_test)

# 평가 지표: 평균
mae = mean_absolute_error(Y_test, Y_pred)
r2 = r2_score(Y_test, Y_pred)


# 예측 데이터 test 출력
new_data = pd.DataFrame([{
    '요일구분': '평일',
    '호선': 1,
    '역번호': 158,
    '출발역': '청량리',
    '상하구분': '상선'
}])[X.columns]  # 컬럼 순서 맞추기

# 예측
predicted = pipeline.predict(new_data)[0]

# 출력
print("\n Predicted 혼잡도:")
for time, value in zip(time_columns, predicted):
    print(f"{time}: {value:.1f}%")

# 데이터 모델 추출
import joblib

# 전체 파이프라인을 저장
joblib.dump(pipeline, 'subway_congestion_model.pkl')
print("모델 저장 완료")

joblib.dump(pipeline, '/content/drive/MyDrive/norush_train_data/subway_congestion_model.pkl')
