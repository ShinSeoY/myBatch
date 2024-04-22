# 오늘의 환율 back-end
환율정보 제공 및 환율 계산, push 알림 프로젝트
<br/>
<br/>

## 사용 스택
JAVA 17 <br/>
Spring boot 3 <br/>
MySQL <br/>
JPA <br/>
JUnit <br/>
Spring boot Batch <br/>
Spring boot Scheduler <br/>
AWS RDS <br/>
Docker <br/>
Docker-compose <br/>
<br/>

## 적용 기술
> - Spring security <br/><br/>
> - 한국수출입은행 Open API 적용 <br/><br/>
> - Spring Batch, Scheduler를 통한 오늘의 환율 정보 업데이트 <br/><br/>
> - AES-256 암호화 <br/><br/>
> - 나의 관심환율 설정 <br/><br/>
> - 환율 계산기 제공 <br/><br/>
> - 환율 알림 설정 시 특정 시간 batch를 통한 SMS 또는 Email 알림 서비스 제공 <br/><br/>

<br/>
<br/>

## 적용 서비스
#### 오늘의 환율 정보 Scheduler Batch flow chart
<img width="588" alt="image" src="https://github.com/ShinSeoY/myBatch/blob/main/img/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-10%20%EC%98%A4%ED%9B%84%203.40.31.png">

#### 환율 알림 서비스 Scheduler Batch flow chart
<img width="588" alt="image" src="https://github.com/ShinSeoY/myBatch/blob/main/img/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-04-10%20%EC%98%A4%ED%9B%84%203.40.23.png">

#### DB ERD Diagram
<img width="588" alt="image" src="https://github.com/ShinSeoY/myBatch/blob/main/img/erd.png">

## 특이 사항
> - application.yml 파일은 secret key 및 db 정보 등을 담고 있어 .gitignore 에 추가함
> - daily batch 실행 중 StepExecutionListener 를 통해 Execution 및 Error를 파악하여 batch_status 테이블에 결과 값 저장
> - Job 수동 실행을 위한 JobLauncherController Junit test 적용
> - EventListener를 사용하여 mail, sms 전송 서비스 async 처리
> - 회원가입시 핸드폰 번호를 AES-256 암호화 방식을 사용하여 암호화한 후 DB에 저장
