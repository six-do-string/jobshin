# jobshin - 모의 면접 서비스

## 목차

1. [📘 프로젝트 소개](#1.-프로젝트-소개)
2. [📝 프로잭트 개요](#2.-프로젝트-개요)
3. [👥 팀 소개](#3.-팀-소개)
4. [✨ 주요 기능](#4.-주요-기능)
5. [🛠️ 기술 스택](#5.-기술-스택)
6. [Flow Chart](#6.-Flow Chart)
7. [📋 요구사항 명세](#7.-요구사항-명세)
8. [📂 폴더 구조](#8.-폴더-구조)
9. [🗂️ ERD](#9.-ERD)
10. [📡 API 명세](#10-API-명세)
11. [🏛️ 아키텍처](#11.-아키텍처)
12. [🖥️ 화면 설계](#12.-화면-설계)
13. [⚠️ 주요 이슈](#13.-주요-이슈)
14. [🔄 회고](#14.-회고)

## 📘 1. 프로젝트 소개

jobsin 프로젝트는 기술 면접을 준비하는 취준생을 타겟팅한 프로젝트로 AlanAI를 활용하여 개인화 된 학습을 지원 하며 사용자의 CS 지식을 향상 시키는데 도움을 주는 온라인
모의 면접 플렛폼 입니다.

## 📝 2. 프로잭트 개요

### 2-1 프로젝트 명

- JobSin(잡신)

### 2-2. 프로젝트 기간

- 2024.08.21 ~ 2024.09.12(3주)

### 2-3. 프로젝트 배포 주소

- **[✨Jobsin](http://3.39.74.113:8080/views/main)**

## 👥 3. 팀 소개

| 이정석               | 조준호            | 윤준호       | 노유진       | 박성환           | 박혜원       |
|-------------------|----------------|-----------|-----------|---------------|-----------|
| 팀장                | 팀원             | 팀원        | 팀원        | 팀원            | 팀원        |
| Infra, BE         | BE, FE         | BE, FE    | BE, FE    | Test          | Test      |
| Docker, AWS, User | User, Security | Interview | Interview | InterviewTest | User Test |

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
    - 2번 항목인 경우 질문에 대한 답변을 제출한 후 다음 문제로 이동할 수 있는 버튼 활성화
- 모의면접 평가
    - 2번인 경우 각 항목별로 점수와, 평가 내용, 면접 키워드 예시 답변/추천 답변 노출

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

## 8. ERD

![img.png](img.png)

## 9. API 명세

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

## 10. 아키텍처

![img_1.png](아키택처.png)

## 11. 화면 설계

| 메인 페이지 (상단) | 메인 페이지 (중단) |
|-------------|-------------|
|![img_2.png](img_2.png)||

## 12. 주요 이슈

프로젝트에서 발생한 주요 이슈들...

## 13. 회고

프로젝트 진행 과정에서 배운 점과 회고...
