# Java 17이 설치된 공식 이미지
FROM eclipse-temurin:17-jdk

# 앱 실행 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY build/libs/norush-0.0.1-SNAPSHOT.jar app.jar

# Spring Boot는 기본적으로 8080 포트 사용
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
