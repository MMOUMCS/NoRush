import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException, Query
from pydantic import BaseModel, Field
from typing import List

app = FastAPI(title="서울 지하철 칸별 혼잡도 예측 API")

# 모델 및 전처리 파이프라인 로드
try:
    pipeline = joblib.load('Seoul_congestion_model.pkl')
except FileNotFoundError:
    print("오류: 모델 파일을 찾을 수 없습니다. train.py를 먼저 실행하세요.")
    exit()

# 칸별 승객 분배 (6칸 기준, 비율은 임의 조정 가능)
def distribute_passengers_to_cars(total_passengers, num_cars=6):
    weights = [0.10, 0.20, 0.25, 0.25, 0.15, 0.05]
    if num_cars != len(weights):
        weights = [1/num_cars] * num_cars
    car_passengers = [total_passengers * w for w in weights]
    return car_passengers

def get_congestion_level(passengers):
    if passengers < 30:
        return "🟢 원활"
    elif passengers < 70:
        return "🟡 보통"
    else:
        return "🔴 혼잡"

# 응답 모델 정의
class CarCongestion(BaseModel):
    car_number: int = Field(..., description="칸 번호", example=1)
    congestion: str = Field(..., description="혼잡도 수준", example="🟢 원활")

class HourlyCongestion(BaseModel):
    hour: str = Field(..., description="시간대", example="05:30-06:30")
    total_predicted_passengers: float = Field(..., description="해당 시간대 예측 혼잡도(%)", example=45.3)
    congestion_by_car: List[CarCongestion]

class PredictionResponse(BaseModel):
    day_type: str = Field(..., description="요일 구분", example="평일")
    line: int = Field(..., description="호선 번호", example=1)
    station_number: int = Field(..., description="역 번호", example=158)
    station_name: str = Field(..., description="출발역 이름", example="청량리")
    direction: str = Field(..., description="상하구분", example="상선")
    predictions: List[HourlyCongestion]

# 시간대 컬럼명 (원본 데이터 기준, 5:30 ~ 00:30)
time_columns = [
    '05:30~06:30', '06:30~07:30', '07:30~08:30', '08:30~09:30', '09:30~10:30',
    '10:30~11:30', '11:30~12:30', '12:30~13:30', '13:30~14:30', '14:30~15:30',
    '15:30~16:30', '16:30~17:30', '17:30~18:30', '18:30~19:30', '19:30~20:30',
    '20:30~21:30', '21:30~22:30', '22:30~23:30', '23:30~00:30'
]

@app.get("/congestion/", response_model=PredictionResponse, summary="특정 역의 시간대별 칸별 혼잡도 예측")
def predict_subway_congestion(
    day_type: str = Query(..., description="요일구분 (예: 평일, 토요일, 일요일)"),
    line: int = Query(..., description="호선 번호 (예: 1, 2, 3)"),
    station_number: int = Query(..., description="역 번호 (예: 158)"),
    station_name: str = Query(..., description="출발역 이름 (예: 청량리)"),
    direction: str = Query(..., description="상하구분 (예: 상선, 하선)")
):
    # 입력값으로 DataFrame 생성
    input_df = pd.DataFrame([{
        '요일구분': day_type,
        '호선': line,
        '역번호': station_number,
        '출발역': station_name,
        '상하구분': direction
    }])

    # 예측
    try:
        preds = pipeline.predict(input_df)[0]  # (시간대별 혼잡도 배열)
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"예측 중 오류 발생: {e}")

    # 각 시간대별로 혼잡도 %와 칸별 분배 계산
    hourly_predictions = []
    for time_str, congestion_pct in zip(time_columns, preds):
        congestion_pct = max(0, congestion_pct)  # 음수 방지
        car_passengers = distribute_passengers_to_cars(congestion_pct)

        car_congestion_list = [
            CarCongestion(car_number=i + 1, congestion=get_congestion_level(p))
            for i, p in enumerate(car_passengers)
        ]

        hourly_predictions.append(HourlyCongestion(
            hour=time_str,
            total_predicted_passengers=congestion_pct,
            congestion_by_car=car_congestion_list
        ))

    return PredictionResponse(
        day_type=day_type,
        line=line,
        station_number=station_number,
        station_name=station_name,
        direction=direction,
        predictions=hourly_predictions
    )
