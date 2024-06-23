finfintech

## 프로젝트 개요
**finfintech**는 핀테크 관련 기능을 제공하는 애플리케이션입니다. 
이 프로젝트는 계좌 검색, 관리, 송금 등의 기능을 포함하고 있으며, 사용자의 계좌 접근을 제어하는 로그인/로그아웃 기능도 구현되어 있습니다.

## 주요 기능

+ **회원가입 기능**
  + ID **(e-mail)**, PASSWORD, 이름, 생년월일, 전화번호를 입력하여 회원가입 가능
  + ID 중복 불가
  + PASSWORD는 bcrypt로 해싱해서 저장
    
+ **로그인 기능**
  + 로그인 후 계좌 생성 가능
  + 계좌번호 자동 생성 (중복 불가)
  + ID, PASSWORD를 입력하여 로그인
  + 로그인시 회원가입에서 생성한 ID와 PASSWORD가 일치 해야함

+ **계좌 관리 기능**
  + 계좌 생성
  + 계좌 삭제
  + 금액 입금
  + 금액 출금
 
+ **계좌 송금 기능**
  + 타인 명의의 계좌로 송금
  + 송금시 존재하지 않는 계좌번호로 송금 불가
  + 송금 내역 조회
  + 송금시 메모 추가 가능 (default -> 보내는이 이름)
    

## 설치 및 실행 방법
### 필수 조건
+ Java 17
+ Spring Boot


## 사용 예제

### 로그인 / 로그아웃
+ 로그인
    POST /login
+ 로그아웃
    POST /logout

### 계좌 검색
+ 특정 계좌를 검색하여 계좌 정보를 조회할 수 있습니다.
    GET /accounts/{accountId}

### 계좌 관리
+ 계좌 생성
    POST /accounts
+ 계좌 삭제
    DELETE /accounts/{accountId}
+ 금액 인출
    POST /accounts/{accountId}/withdraw
+ 금액 입금
    POST /accounts/{accountId}/deposit

### 송금 기능 및 이력 조회
+ 송금 기능
    POST /transfer
+ 송금 이력 조회
    GET /transfer/history

