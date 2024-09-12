# jobshin - 모의 면접 서비스

## 목차

[1. 📘 프로젝트 소개](#1-프로젝트-소개)  
[2. 📝 프로잭트 개요](#2-프로젝트-개요)  
[3. 👥 팀 소개](#3-팀-소개)  
[4. ✨ 주요 기능](#4-주요-기능)  
[5. 🛠️ 기술 스택](#5-기술-스택)  
[6. 💹 Flow Chart](#6-Flow-Chart)  
[7. 📋 요구사항 명세](#7-요구사항-명세)  
[8. 📂 폴더 구조](#8-폴더-구조)  
[9. 🗂️ ERD](#9-ERD)  
[10. 📡 API 명세](#10-API-명세)  
[11. 🏛️ 아키텍처](#11-아키텍처)  
[12. 🖥️ 화면 설계](#12-화면-설계)  
[13. ⚠️ 주요 이슈](#13-주요-이슈)  
[14. 🔄 회고](#14-회고)

## 1. 프로젝트 소개

jobsin 프로젝트는 기술 면접을 준비하는 취준생을 타겟팅한 프로젝트로 AlanAI를 활용하여 개인화 된 학습을 지원 하며 사용자의 CS 지식을 향상 시키는데 도움을 주는 온라인
모의 면접 플렛폼 입니다.

## 2. 프로잭트 개요

### 2-1 프로젝트 명

- JobSin(잡신)

### 2-2. 프로젝트 기간

- 2024.08.21 ~ 2024.09.12(3주)

### 2-3. 프로젝트 배포 주소

- **[✨Jobsin](https://3.39.74.113.nip.io/views/main)**
테스트 계정: user@test.com
테스트 비밀번호: kjccs123!@#

## 3. 팀 소개

| 이정석               | 조준호            | 윤준호       |
|-------------------|----------------|-----------|
|<a href="https://github.com/LeeJeongSeok" target="_blank"><img src="https://github.com/LeeJeongSeok.png" width="100" height="100" alt="이정석"></a> | <a href="https://github.com/whwnsgh0258" target="_blank"><img src="https://github.com/whwnsgh0258.png" width="100" height="100" alt="조준호"></a> | <a href="https://github.com/yunjunho97" target="_blank"><img src="https://github.com/yunjunho97.png" width="100" height="100" alt="윤준호"></a> |
| 팀장                | 팀원             | 팀원        |
| Infra, BE         | BE, FE         | BE, FE    |
| Docker, AWS, User | User, Security | Interview |

| 노유진       | 박성환           | 박혜원       |
|-----------|---------------|-----------|
|<a href="https://github.com/shdbwls66" target="_blank"><img src="https://github.com/shdbwls66.png" width="100" height="100" alt="노유진"></a> | <a href="https://github.com/yub-bi" target="_blank"><img src="https://github.com/yub-bi.png" width="100" height="100" alt="박성환"></a> | <a href="https://github.com/wions37" target="_blank"><img src="https://github.com/wions37.png" width="100" height="100" alt="박혜원"></a> |
| 팀원        | 팀원            | 팀원        |
| BE, FE    | Test          | Test      |
|Interview | InterviewTest | User Test |

## 4. 주요 기능

1. 기술 면접을 준비하는 취준생들을 위하여 AlanAI를 활용하여 언어 및 포지션 별로 모의 면접 서비스 제공 합니다.
2. 모의 면접 시 특정한 분야(Language, Network, Algorithm, OS 등등)에 대한 연습을 할 수 있는 연습모드와 모든 분야를 통합하여 다양한 질문을 받을 수
   있는 실전 모드로 나누어서 제공 합니다.
3. 사용자 마다 Level을 부여하여 해당 Level에 맞는 질문을 받고 실전 모드의 총 점수와 AI 피드백 점수를 합하여 Level을 향상 시킬 수 있습니다.

## 5. 기술 스택

### 5-1. BackEnd

<div>
    <img alt="Static Badge" src="https://img.shields.io/badge/SpringBoot-%236DB33F?style=flat-square&logo=springboot&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/springsecurity-%236DB33F?style=flat-square&logo=springsecurity&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/mysql-%234479A1?style=flat-square&logo=mysql&logoColor=%23FFFFFF"><br>
    <img alt="Static Badge" src="https://img.shields.io/badge/gradle-%2302303A?style=flat-square&logo=gradle&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/Java-%236DB33F?style=flat-square&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/SpringDataJPA-%236DB33F?style=flat-square&logo=SpringDataJPA&logoColor=%23FFFFFF">
</div>

### 5-2. FrontEnd

<div>
    <img alt="Static Badge" src="https://img.shields.io/badge/html5-%23E34F26?style=flat-square&logo=html5&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/css3-%231572B6?style=flat-square&logo=CSS3&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/javascript-%23F7DF1E?style=flat-square&logo=javascript&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/thymeleaf-%23005F0F?style=flat-square&logo=thymeleaf&logoColor=%23FFFFFF">
</div>

### 5-3. Infra

<div>
    <img alt="Static Badge" src="https://img.shields.io/badge/amazonec2-%23FF9900?style=flat-square&logo=amazonec2&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/docker-%232496ED?style=flat-square&logo=docker&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/AlanAI-%23221E68?style=flat-square&logo=AlanAI&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/amazonrds-%23527FFF?style=flat-square&logo=amazonrds&logoColor=%23FFFFFF">
</div>

### 5-4. Etc(Tool, Community)

<div>
    <img alt="Static Badge" src="https://img.shields.io/badge/discord-%235865F2?style=flat-square&logo=discord&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/notion-%23000000?style=flat-square&logo=notion&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/github-%23181717?style=flat-square&logo=github&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/git-%23F05032?style=flat-square&logo=git&logoColor=%23FFFFFF">
    <img alt="Static Badge" src="https://img.shields.io/badge/intellijidea-%23000000?style=flat-square&logo=intellijidea&logoColor=%23FFFFFF">
</div>

## 6. Flow Chart

![image](https://github.com/user-attachments/assets/bb1d34c2-9097-4b64-a519-9f81291a0b23)



## 7. 요구사항 명세

### 7-1 유저 요구사항

#### 유저 회원가입

- 입력값 : 이메일(아이디), PW, 이름, 분야, 주 언어
- 정책 - 생성 (유효성 검사)
    - 아이디는 유효성 검사를 진행하고, 회원가입시 중복 체크를 진행
        - 입력값이 이메일형태인지 확인, 빈 값이 안된다.
        - DB에 해당 아이디가 존재한 상태에서 회원 가입시 ‘이미 존재하는 회원’의 메시지 출력
    - 비밀번호는 최소 8 ~ 16자 이하이며, 영문 대/소문자, 숫자, 특수문자로 구성 **(2개 이상을 조합해서 가입)**
    - 이름은 공백 체크만 한 후 길이만 체크 (최소 2자, 최대 6자)
    - 분야와 주 언어는 selectBox로 구현
        - 기본값
            - 분야: Backend
            - 주 언어: Java
    - 최초 회원가입이 완료되면 가입 일시와 수정 일시 업데이트
    - 회원 수정시 수정 일시만 업데이트
    - 위 정책을 통과하면 회원가입이 완료되고, 메인 페이지로 이동한다.

#### 유저 로그인

- 사용자 로그인 기능 (이메일과 비밀번호 사용)
    - 로그인 실패 시 “아이디 혹은 비밀번호가 맞지 않습니다.” 노출
- checkBox를 활용하여 remember-me 기능 여부 체크
    - remember-me가 활성화 되는 경우 최대 일주일동안 유저 세션 보관

#### 유저 정보 수정

- 이메일(아이디)을 제외한 나머지 항목에 대해서 수정 가능
- 이메일(아이디)을 제외한 나머지 항목 회원가입과 마찬가지로 정책 및 유효성 검사 동일하게 진행
- 수정이 성공적으로 이뤄지면 해당 유저 데이터의 수정 일시 컬럼을 업데이트

#### 유저 이력 관리

- 테이블 뷰 형태로 면접을 진행한 이력을 보관한다. (리스트 형식)
- 상세보기로 이동 시 n회차에 대한 평가지표 노출 (하단 참고)
    - 출제되었던 문제
    - 제출했던 답변
    - 점수
    - 예시 답변 / 추천 답변
    - 이력 관리 상단에 낮은 점수 3개를 뽑아서 나타낸다.
    - 카테고리별 사용자의 평점
        - 모의 면접 질문 5개를 카테고리별로 나눠서 질문을 던지도록 설정

#### 유저 탈퇴

- hard delete
- 탈퇴한 유저가 재가입시도 시, 재가입 가능
- 탈퇴한 유저가 가진 정보는 다 삭제

### 7-2 모의면접 요구사항 (실전 모드)

- 모의면접 출제
    - 사용자 정보를 기반으로 기술 면접에 필요한 문제를 출제
    - 면접자(앨런 AI)가 문제를 출제하면, 지원자(사용자)가 답변을 제출하는 방식
    -  답변을 제출한 후 다음 문제로 이동할 수 있는 버튼 활성화
- 모의면접 평가
    - 면접 점수와, 평가 내용, 면접 키워드 예시 답변/추천 답변 노출

### 7-3 카테고리별 모의면접 (연습 모드)

- 카테고리별 문제 출제
    - 사용자가 카테고리(cs, 네트워크, 알고리즘 등)을 선택한 후 필요한 문제를 출제
    - 진행방식은 실전 모드와 동일

### 7-4 AI 서비스에 대한 평가

- 모의면접이 완료가 되면 사용자로부터 AI 서비스에 대한 피드백을 받을 수 있음
- 모의면접 완료 페이지에 별도의 평가하기 버튼을 만들어 사용자로부터 피드백을 받는다.
    - 피드백이 전송되면, 관련된 내용을 학습 시킨다.
- 타겟 설정
    - 질문에 대한 피드백 (상/중/하)
- 피드백 주기
    - 유저 레벨을 나눈다. (대략 3단계 정도로 나눈다 → 초급, 중급, 고급)
    - 질문이 너무 어렵다 싶으면 유저 레벨을 낮춘다.
    - 질문이 쉽다 싶으면 유저 레벨을 높인다.
    - (피드백 받은 유저 체감 난이도 점수(어려웠다0, 보통50, 쉬웠다100) + 실제 평균점수(0~100)) / 2 ⇒ 40~60 유지
    - 점수를 기준으로 레벨 반영
    - 실전 모드만 반영
 
### 7-5 공통 요구사항

- **페이징 처리**
    - 면접 이력 페이지에 페이징 기능 추가
- **정렬 기능**
    - 면접 이력 페이지에 최신날짜 기준으로 내림차순
    - 상단에는 완료되지 않은 Interview를 배치시키고 완료된 인터뷰 표시
- **유효성 검사**
    - 입력 데이터에 대한 유효성 검사 (이메일 형식, 비밀번호 등).
- **공통 예외 처리**
    - API 호출 시 발생할 수 있는 예외 처리 (예: 게시글이 존재하지 않음, 권한 없음 등).
- **보안**
  - Spring Security 기반의 폼 로그인을 사용해 사용자 인증 처리
  - Remember-Me 기능을 구현해 사용자가 로그인 상태를 유지할 수 있도록 설정
  - 비밀번호는 BCrypt 암호화 방식으로 저장

## 8. 폴더 구조

### 8-1 BackEnd

#### Domain

- user: 회원과 관련된 로직을 작성하는 폴더

```text
├── controller
│   └── UserController.class
├── domain
│  ├── User$UserBuilder.class
│  └── User.class
├── dto
│  ├── CreateUserRequest.class
│  ├── LoginRequest.class
│  ├── MyPageInterviewDetailDto.class
│  ├── MyPageInterviewWithDetailsDto.class
│  ├── UpdateUserRequest.class
│  └── UserResponse.class
├── repository
│   └── UserRepository.class
├── service
│   └── UserService.class
└── util
      ├── BaseEntity.class
      ├── Language.class
      ├── Level.class
      └── Position.class
```

- Interview: 모의 면접 진행과 관련된 폴더

```text
 interview
 ├── controller
 │   ├── InterviewController.class
 │   └── InterviewThymeleafController.class
 ├── domain
 │  └── Interview.class
 ├── dto
 │   ├── InterviewDto.class
 │   └── InterviewHistorySummaryResponse.class
 ├── repository
 │   └── InterviewRepository.class
 └── service
      └── InterviewService.class
```

- InterviewDetails: 모의 면접 진행 내용(질문, 답 등)과 같은 상세 정보와 관련된 폴더

```text
interviewDetail
 ├── domain
 │   └── InterviewDetail.class
 ├── dto
 │   ├── InterviewDetailDto.class
 │   ├── InterviewQuestion.class
 │   └── InterviewResultDetail.class
 ├── repository
 │   └── InterviewDetailRepository.class
 ├── service
 │   └── InterviewDetailService.class
 └── util
     ├── Category.class
     └── Mode.class

```

#### global

- common: 실패 또는 성공한 요청에 응답을 관리하는 폴더

```text
├── common
│   ├── ApiResponse$ApiResponseBuilder.class
│   ├── ApiResponse.class
│   ├── ErrorResponse$ErrorResponseBuilder.class
│   ├── ErrorResponse$FieldError$FieldErrorBuilder.class
│   ├── ErrorResponse$FieldError.class
│   └── ErrorResponse.class
```

- error: 에러 코드와 글로벌로 예외처리를 관리하는 폴더

```text
├── error
│   ├── ErrorCode.class
│   └── GlobalExceptionHandler.class
```

- security: 인증/인가 로직을 구현한 폴더

```text
└── security
│       ├── config
│       │   ├── AuthConfig.class
│       │   └── SecurityConfig.class
│       ├── handler
│       │   └── CustomAuthenticationFailureHandler.class
│       ├── model
│       │   └── CustomUserDetails.class
│       ├── provider
│       │   └── UserAuthenticationProvider.class
│       └── service
│           └── UserDetailService.class
```

#### infra

- alan: AlanAI의 API를 사용하기 위한 폴더

```text
└──alan
│  ├── AlanService.class
│  └── PromptMessage.class
```

### 8-2 FrontEnd

#### HTML

```text
└──templates
│   ├── AlFeedback.html
│   ├── error.html
│   ├── interview
│   │   ├── interviewMainPage.html
│   │   ├── interviewPracticeEnter.html
│   │   ├── interviewQuestion.html
│   │   └── interviewRealEnter.html
│   ├── layout
│   │   ├── header.html
│   │   └── main.html
│   ├── levelFeedback.html
│   └── user
│   │   ├── edit.html
│   │   ├── login.html
│   │   ├── mypage.html
│   │   ├── practice_interview_detail.html
│   │   ├── practice_interview_list.html
│   │   ├── real_interview_detail.html
│   │   ├── real_interview_list.html
│   │   └── signup.html
```

#### CSS

```text
├── css
│   ├── AlFeedback.css
│   ├── LevelFeedback.css
│   ├── detail.css
│   ├── header.css
│   ├── interviewMainPage.css
│   ├── interviewPracticeEnter.css
│   ├── interviewQuestion.css
│   ├── interviewRealEnter.css
│   ├── main.css
│   ├── mypage.css
│   ├── page.css
│   ├── sidebar.css
│   ├── table.css
│   └── user.css
```

## 9. ERD

![Untitled](https://github.com/user-attachments/assets/7a93e4b2-07d3-4edc-a7d7-ab9f24de79a1)


## 10. API 명세

### 📝 회원 가입

| 🏷️ Name       | ⚙️ Method | 📎 URL                      | 📑 Description        |
|----------------|-----------|-----------------------------|-----------------------|
| userSignUpForm | GET       | /views/users/signup         | 일반 회원 가입 폼 요청         |
| userSignUp     | POST      | /api/users/signup           | 일반 회원 가입 요청           |
| userDelete     | DELETE    | /api/users/delete{username} | 회원 탈퇴 요청(RESTful API) |

### 🔐 로그인

| 🏷️ Name      | ⚙️ Method | 📎 URL             | 📑 Description |
|---------------|-----------|--------------------|----------------|
| userLoginForm | GET       | /views/users/login | 일반 회원 로그인 폼 요청 |
| userLogout    | POST      | /api/users/logout  | 일반 회원 로그아웃     |

### 🧑 유저 정보

| 🏷️ Name     | ⚙️ Method | 📎 URL            | 📑 Description   |
|--------------|-----------|-------------------|------------------|
| userEditForm | GET       | /views/users/edit | 일반 회원 정보 수정 폼 요청 |
| userEdit     | PUT       | /api/users/edit   | 일반 회원 수정 요청      |

### 🧑 마이페이지

| 🏷️ Name            | ⚙️ Method | 📎 URL                               | 📑 Description         |
|---------------------|-----------|--------------------------------------|------------------------|
| userMyPage          | GET       | /views/users/edit                    | 마이페이지                  |
| listInterviews      | GET       | /views/users/interviews/practice     | 모의 면접 이력 리스트 보기(연습 모드) |
| realInterviews      | GET       | /views/users/interviews/real         | 모의 면접 이력 리스트 보기(실전 모드) |
| realInterviewDetail | GET       | /views/users/interviews/practice{id} | 모의 면접 이력 상세 보기(연습 모드)  |
| realInterviewDetail | GET       | /views/users/interviews/real/{id}    | 모의 면접 이력 상세 보기(실전 모드)  |

### 💬인터뷰 생성 (RESTful API)

| 🏷️ Name                       | ⚙️ Method | 📎 URL                        | 📑 Description |
|--------------------------------|-----------|-------------------------------|----------------|
| createPracticeInterview        | POST      | /api/mock-interviews/practice | 모의 면접 연습 문제 생성 |
| createRealInterview            | POST      | /api/mock-interviews/real     | 모의 면접 실전 문제 생성 |
| getNextQuestion                | GET       | /api/mock-interviews/next     | 다음 문제 생성       |
| submitAnswerAndGetNextQuestion | POST      | /api/mock-interviews/next     | 답변 후 다음 문제 생성  |
| finish                         | POST      | /api/mock-interviews/finsh    | 모의 면접 종료       |

### 🎸 기타

| 🏷️ Name                    | ⚙️ Method | 📎 URL                                       | 📑 Description |
|-----------------------------|-----------|----------------------------------------------|----------------|
| summary                     | GET       | /api/mock-interviews/summary                 | 모의 면접 요악       |
| continueIncompleteInterview | GET       | /api/mock-interviews/incomplete/{interviewId | 공지사항 생성 요청     |
| deleteInterview             | DELETE    | /api/mock-interviews/{interviewId}           | 모의 면접 삭제       |

## 11. 아키텍처

![image](https://github.com/user-attachments/assets/c3ff827b-764d-4ad2-8402-808ad8497f1d)

## 12. 화면 설계

| 메인 페이지 (상단) | 메인 페이지 (중단) |
|-------------|-------------|
|<img width="1470" alt="메인 페이지 상단" src="https://github.com/user-attachments/assets/4e69cebe-341f-4202-9059-96894e03eecc">|<img width="1470" alt="메인페이지 중앙" src="https://github.com/user-attachments/assets/cb1d2d0a-bef4-49d2-8bab-f5af457b14d3">|
| 메인 페이지 (하단) | 로그인 페이지 |
|<img width="1470" alt="메인 페이지 하단" src="https://github.com/user-attachments/assets/04bb842b-90c5-41d8-a756-f17f86d0a91e">|<img width="1470" alt="로그인 페이지 최종" src="https://github.com/user-attachments/assets/794a7b2b-8fbf-499b-a506-3b8355d49ef7">|
| 회원가입 페이지 | 마이페이지 |
|<img width="1470" alt="image" src="https://github.com/user-attachments/assets/0474649c-01b9-4f36-9bbf-e5e995d616a6" alt="회원가입 페이지">|<img width="1470" src="https://github.com/user-attachments/assets/5711f624-0e5a-4c7d-93fb-f5f3773b488a" alt="마이 페이지">|
| 회원 정보 수정 페이지 | 인터뷰 메인 페이지 |
|<img width="1470"  src="https://github.com/user-attachments/assets/154e641e-1dd1-4e89-8dd7-8db649ef15a7" alt="회원정보 수정 페이지">|<img width="1470" alt="스크린샷 2024-09-11 오후 3 46 58" src="https://github.com/user-attachments/assets/4c21fa56-0863-4e01-b823-206517c16029">|
| 연습모드 이력 리스트 | 실전모드 이력 리스트 |
|<img width="1470" src="https://github.com/user-attachments/assets/068ee736-2699-4d2a-be0e-28211dc2cced" alt="연습모드 이력 리스트 조회">|<img width="1470" src="https://github.com/user-attachments/assets/241b5426-acb3-41ae-9d44-ac805ea6eaf2" alt="실전모드 이력 리스트 조회">|
| 연습모드 이력 상세 | 실전 모드 이력 상세 |
|<img width="1470" alt="연습모드 이력 상세" src="https://github.com/user-attachments/assets/adedd59e-e568-4348-ad5d-c73edee0bb4f">|<img width="1470" src="https://github.com/user-attachments/assets/1cecb3f9-bcb0-471b-844a-df1d5c232c05" alt="실전모드 이력 상세">|
| 모의면접 (연습모드) 시작하기 | 모의면접 (실전모드) 시작하기|
|<img width="1470" alt="연습모드 시작" src="https://github.com/user-attachments/assets/e14906d1-4bea-4b1b-9367-a37210cc4f85">|<img width="1470" alt="실전모드 시작" src="https://github.com/user-attachments/assets/48263641-9e40-4766-aff2-7c5c90a248c9">|
| 모의면접 진행 | 모의면접 (실전모드) 완료 후 피드백 |
|<img width="1470" alt="모의 면접 진행" src="https://github.com/user-attachments/assets/414b7dd2-416a-460a-8484-53d74b87ba78">|<img width="1470" alt="AI 피드백" src="https://github.com/user-attachments/assets/40553944-cc9f-4224-9011-cafa15dde552">|

## 13. 주요 이슈
### User
- **Issue**
  1. GET 방식으로 로그아웃 기능을 작성 했으나 로그아웃 기능이 정상적으로 동작하지 않는 문제 발생
  2. 점수가 null을 포함하는 interview 데이터에서 평균 점수를 계산하는 과정에서 오류 발생
- **해결**
  1. POST 방식으로 로그아웃 요청을 변경하여 문제를 해결하고, 보안성 강화
  2. MyPageInterviewDetailDto 클래스에서 점수가 null일 경우 0으로 처리하여 문제 해결

### Interview
- **Issue**
  1. 면접 질문 생성과 처리를 동기적으로 처리하여 답변 처리가 완료될 때 까지 다음질문이 생성되지 않아 흐름이 끊기는 문제 발생
  2. 면접 질문 생성 과정에서 카테고리 배열이 고정되어 있어 다양성이 부족한 문제 발생
- **해결**
  1. 비동기 처리를 적용하여 답변이 완료되지 않아도 다음 질문을 즉시 전달하여 면접의 진행 흐름 개선
  2. 2. 카테고리 배열을 랜덤하게 생성하는 로직을 도입하여 질문의 다양성을 확보

### Infra
- **Issue**
  1. GitHub Actions가 실행 될 때 마다 EC2 보안 그룹에 IP를 추가하는 구조로 구성 하다보니 GitHub Actions가 실패하게 되면 보안 그룹에 IP가 추가되어 더이상 사용하지 않는 형태로 남게되는 문제 발생.
- **해결**
  1. GitHub Actions가 IP를 자동 보안 그룹에서 제거하는 구조로 변경하였고, 자동으로 보안 그룹을 정리하여서 보안성과 관리 효율성을 향상 시켰다.
 
### Test
- **Issue**
  1. 사용자 정보를 DB에 저장하는 과정에서 ID가 생성되지 않거나 조회에 실패하는 문제 발생
  2. Interview와 User 엔티티 간의 연관관계 매핑에서 오류 발생
  3. 유효성 검사가 부족하여 잘못된 데이터가 처리되는 분제 발생
- **해결**
  1. JPA 영속성 관리를 재확인 하고, SaveAndFlush를 사용하여 즉시 저장 및 Id 생성 여부를 검증. 이후 findById로 사용자를 조회하는 과정에서의 문제 해결
  2. 엔티티 관계를 수정하고, 사용자 객체가 인터뷰 생성 시 정확하게 할당 되도록 개선
  3. 기능별로 세분화된 유효성 검사 및 예외 처리 로직을 추가하여 문제 해결 및 안전성 향상

## 14. 회고
### 이정석
- 간단한 CI/CD 과정이라고 해도 하나의 파이프라인을 구축한다는 것은 쉽지 않았지만, 한번 제대로 구축해놓으면 빠르게 개발할 수 있는 아이템 인것 같다.
### 조준호
- 시간 관리에 중요성을 느꼇고 인증 로직과 같은 핵심 기능은 프로젝트 초기에 확실하게 설계해야한다고 느꼇다. 또한 빠른 구현과 완성도 높은 구현을 동시에 추구하는 것은 어렵고, 스스로 타협하는 순간이 많았던것 같다.
### 박혜원
- 테스트 과정을 통해 코드 흐름에 대한 이해도가 향상 되었고, 코드 수정시 테스트 코드의 영향으로 디버깅에 많은 시간을 소요한것 같아서 보다 더 효율적인 진행을 위해 개선이 필요한것 같다.
### 윤준호
- 구조가 복잡해 질수록 설계단계에서 놓친 부분이 크게 다가오는것 같다. 구현을 시작하기 전에 설계단게에서 좀 더 고민해서 로직의 논리적 오류나 효율성에 대해 좀 더 생각할 수 있으면 좋을것 같다.
### 노유진
- 프로젝트를 진행 하면서 코드를 어떤 식으로 작성하고, 흐름은 어떻게 되는지, 그리고 프로젝트를 진행할 때 협업 도구를 어떻게 활용할 수 있는지에 대해 배울수 있었다.
### 박성환
- 다른 사람들이 작성한 코드를 보며 새로운 함수들을 배울 수 있었던 시간이었다. 그리고 개발을 진행할 때 특정한 방범을 고집하지 않고 넓게 바라보며 더 나은 방법을 배울 수 있었다.
