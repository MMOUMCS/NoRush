# 작성일자: 2025-06-13
# 최종수정일자 : 2025-06-14
# 작성자 : 심준서

import joblib
import pandas as pd
from fastapi import FastAPI, HTTPException
from datetime import datetime
from pydantic import BaseModel, Field
from typing import List

app = FastAPI(title="대구 지하철 칸별 혼잡도 예측 API")

try:
    model = joblib.load('lgbm_ridership_model.pkl')
    encoder = joblib.load('station_label_encoder.pkl')
except FileNotFoundError:
    print("오류: 모델/인코더 파일을 찾을 수 없습니다. train.py를 먼저 실행하세요.")
    exit()

def distribute_passengers_to_cars(total_passengers, num_cars=6):

    weights = [0.10, 0.20, 0.25, 0.25, 0.15, 0.05] # 1, 2, 3, 4, 5, 6칸
    
    if num_cars != len(weights):
        weights = [1/num_cars] * num_cars
        
    car_passengers = [total_passengers * w for w in weights]
    return car_passengers

def get_congestion_level(passengers):
    
    if passengers < 30: # 30명
        return "🟢 원활"
    elif passengers < 70: # 70명
        return "🟡 보통"
    else:
        return "🔴 혼잡"

class CarCongestion(BaseModel): # 차량 혼잡도
    car_number: int = Field(..., description="칸 번호", example=1)
    congestion: str = Field(..., description="혼잡도 수준", example="🟢 원활")

class HourlyCongestion(BaseModel): # 시간대 혼잡도
    hour: str = Field(..., description="시간대", example="08시-09시")
    total_predicted_passengers: int = Field(..., description="해당 시간 총 예측 승차인원", example=750)
    congestion_by_car: List[CarCongestion]

class PredictionResponse(BaseModel):
    station: str
    date: str
    predictions: List[HourlyCongestion]

@app.get("/congestion/{station_name}", response_model=PredictionResponse, summary="특정 역의 칸별 혼잡도 예측")
def predict_station_congestion(station_name: str, year: int, month: int, day: int):
    """
    지정한 역과 날짜의 시간대별/칸별 혼잡도를 예측합니다.
    - **station_name**: 예측할 역 이름 (예: 반월당역)
    - **year, month, day**: 예측할 날짜 (예: 2025, 6, 13)
    """
    try:
        station_encoded = encoder.transform([station_name])[0]
    except ValueError:
        raise HTTPException(status_code=404, detail=f"'{station_name}' 역 정보를 찾을 수 없습니다.") # 이 경우 역 이름 잘못 적음

    try:
        date_obj = datetime(year, month, day)
        day_of_week = date_obj.weekday()
    except ValueError:
        raise HTTPException(status_code=400, detail="잘못된 날짜 형식입니다.") # 날짜 잘못 적은 경우

    features = ['월', '일', '요일', '시간', '역명_encoded']
    hourly_predictions = []

    for hour in range(5, 25):
        predict_df = pd.DataFrame([[month, day, day_of_week, hour, station_encoded]], columns=features)

        total_passengers = model.predict(predict_df)[0]
        total_passengers = max(0, int(total_passengers)) # 음수 방지

        car_passengers = distribute_passengers_to_cars(total_passengers)

        car_congestion_list = [] # 차량 혼잡도 부여
        for i, passengers in enumerate(car_passengers):
            car_congestion_list.append(CarCongestion(
                car_number=i + 1,
                congestion=get_congestion_level(passengers)
            ))

        hourly_predictions.append(HourlyCongestion( # 1시간 간격으로
            hour=f"{hour:02d}시-{(hour+1):02d}시",
            total_predicted_passengers=total_passengers,
            congestion_by_car=car_congestion_list
        ))

    return PredictionResponse(   # 역 이름 이랑 시간 반환
        station=station_name,
        date=date_obj.strftime("%Y-%m-%d"),
        predictions=hourly_predictions
    )