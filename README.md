# 📅 Spring Basic Schedule

---

## 📚 Project Overview
Spring JDBC를 활용한 일정 관리 애플리케이션입니다.

- **목적**: 3 Layer Architecture 및 CRUD 필수 기능(JDBC) 연습
- **핵심 기능**:
    - 일정 생성, 조회, 수정, 삭제 기능 제공
    - 작성자와 일정 간의 관계 설정 (FK 활용)
    - 페이징 기능 적용
    - 예외 처리 및 입력 검증 수행

---

## 📝 Table of Contents
1. [Project Structure](#-project-structure)
2. [Features](#-features)
3. [ERD](#-erd)
4. [API Documentation](#-api-documentation)
5. [Tech Stack](#-tech-stack)
6. [Getting Started](#-getting-started)

---

## 🛠 Project Structure
```
src
└── main
    ├── java
    │   └── com
    │       └── example
    │           └── schedule
    │               ├── controller
    │               ├── dto
    │               ├── entity
    │               ├── exception
    │               ├── repository
    │               ├── service
    │               ├── SpringBasicScheduleApplication.java
    ├── resources
    │   ├── application.properties
build.gradle
schedule.sql
```

---

## ✨ Features
### **Lv 1: 일정 생성 및 조회**
- 데이터를 전달받아 일정 생성 
- 전체 일정 목록 조회 및 조건별 조회
- 특정 일정 ID로 개별 일정 조회

### **Lv 2: 일정 수정 및 삭제**
- 선택한 일정의 **할 일(task), 작성자명(authorName)** 수정
  - 비밀번호를 확인 후 수정
  - 수정 시 작성일(updatedAt) 갱신
- 일정 삭제 기능 제공
  - 비밀번호를 확인 후 삭제

### **Lv 3: 연관 관계 설정**
- 작성자와 일정 간 관계 설정 (Foreign Key 활용)
- 작성자 정보 (이름, 이메일, 등록일, 수정일) 관리
  - 생성, 조회 API

### **Lv 4: 페이징 기능**
- 일정 목록 조회 시 페이징 처리 지원
- 페이지 번호 및 크기 기준으로 데이터 조회 및 반환

### **Lv 5: 예외 발생 처리**
- HTTP 상태 코드 및 에러 메시지 반환
- @RestControllerAdvice를 통한 공통 예외 처리

### **Lv 6: Null 체크 및 입력 검증**
- 할 일(task) 200자 제한
- 비밀번호 필수 입력
- 이메일 형식 검증

---

## 🖇️ ERD
![Image](https://github.com/user-attachments/assets/830f4fc2-cb39-4b85-ace0-9bb9b8970d41)

---

## 📜 API Documentation
### 일정 관리 API
![Image](https://github.com/user-attachments/assets/bc9795ab-dd0f-47f0-8171-6270680c776c)

### 작성자 관리 API
![Image](https://github.com/user-attachments/assets/f57f5165-2b4a-47a7-ae6f-f3ab041ea1b5)

**Swagger UI 링크**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🧑‍💻 Tech Stack
- **Backend**: Spring Framework, Spring JDBC
- **Database**: MySQL
- **Build Tool**: Gradle

---

## 🚀 Getting Started
### 1. 저장소 클론
```bash
https://github.com/mannaKim/spring-basic-schedule.git
```
### 2. 환경 설정
- `application.properties` 파일을 설정하여 DB 연결 정보 입력
- `schedule.sql` 파일의 sql을 실행하여 테이블 생성
### 3. 프로젝트 실행
### 4. API 테스트
- Swagger UI 접속: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

