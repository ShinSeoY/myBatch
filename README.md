# 오늘의 환율 back-end              

환율정보 제공 및 환율 계산, Email SMS 환율 알림 프로젝트
<br/>
<p align="center">
<img width="1000" alt="image" src="https://github.com/ShinSeoY/myBatch/blob/main/img/banner.png" />
</p>
<br/>

## 사용 스택
JAVA 17 <br/>
Spring boot 3 <br/>
MySQL <br/>
JPA <br/>
JUnit <br/>
Spring boot Batch <br/>
Spring boot Scheduler <br/>
AWS EC2 <br/>
AWS RDS <br/>
Docker <br/>
Docker-compose <br/>
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
> - daily batch 실행 중 StepExecutionListener 를 통해 Execution 및 Error를 파악하여 batch_status 테이블에 batch 상태 및 결과 값 저장
> - Job 수동 실행을 위한 JobLauncherController Junit test 적용
> - EventListener를 사용하여 mail, sms 전송 서비스 async 처리
> - 회원가입시 핸드폰 번호를 AES-256 암호화 방식을 사용하여 암호화한 후 DB에 저장
