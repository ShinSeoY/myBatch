# 환율 계산기 back-end
환율정보 제공 및 환율 계산, push 알림 프로젝트
<br/>
<br/>

## 사용 스택
JAVA 17 <br/>
Spring boot 3 <br/>
MariaDB <br/>
JPA <br/>
JUnit <br/>
Spring boot Batch <br/>
Spring boot Scheduler <br/>
Docker <br/>
Docker-compose <br/>
<br/>

## 적용 기술
> - Spring security <br/><br/>
> - 한국수출입은행 Open API 적용 <br/><br/>
> - Spring Batch, Scheduler를 통한 오늘의 환율 정보 업데이트 <br/><br/>
> - 나의 관심환율 설정 <br/><br/>
> - 환율 계산기 제공 <br/><br/>
> - 환율 알림 설정 시 특정 시간 batch를 통한 SMS 또는 Email 알림 서비스 제공 <br/><br/>

<br/>
<br/>

## 적용 서비스
#### 오늘의 환율 정보 Scheduler Batch
<img width="588" alt="image" src="https://github.com/ShinSeoY/myBatch/assets/63282412/44224c55-ebfc-44dc-aa02-c514c3888356">

#### 환율 알림 섭비스 Scheduler Batch


#### DB ERD

## 특이 사항
> - application.yml 파일은 secret key 및 db 정보 등을 담고 있어 .gitignore 에 추가함
> - daily batch 실행 중 StepExecutionListener 를 통해 Execution 및 Error를 파악하여 batch_status 테이블에 결과 값 저장
> - Job 수동 실행을 위한 JobLauncherController Junit test 적용
