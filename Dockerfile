## 애플리케이션 실행(jar파일 생성)
## OpenJDK 17를 기반으로 하는 이미지를 사용하며, 이 이미지를 'builder'라는 이름으로 지정
#FROM openjdk:17-jdk-slim AS builder
#
## 작업 디렉토리를 '/app'으로 설정
#WORKDIR /app
#
## 작업토리로 모든 파일을 복사
#COPY . .
#
## gradle을 사용하여 springboot application 빌드
## 이 작업을 통해 build/libs 디렉토리에 jar파일이 생성됨
#RUN chmod +x ./gradlew
#RUN ./gradlew clean bootJar
#
## jar 파일 실행
## OpenJDK 17를 기반으로 하는 이미지를 사용
#FROM openjdk:17-jdk-slim
#
## 작업 디렉토리를 '/app'으로 설정
#WORKDIR /app
## 이전 빌드 단계에서 생성된 JAR 파일을 현재 작업 디렉토리로 복사
#COPY --from=builder /app/build/libs/batch-*.jar app.jar

# ----------- jeknins사용안할때 여기부분 주석처리하고 위에 부분 주석 해제하고 사용 -------------
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/batch-*.jar app.jar
# ------------------------------------------------------------------------------

# 환경 변수 'PROFILE'을 'dev'로 설정
ENV PROFILE="dev"

# 컨테이너가 시작될 때 실행되는 명령어를 지정
# 여기서는 Spring Boot 애플리케이션을 JAR 파일로 실행 & 활성화된 프로파일을 'dev'로 설정
#ENTRYPOINT ["java","-jar","--spring.profiles.active=$PROFILE","/app/app.jar"]
ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE

EXPOSE 8082
