# moa-backend

청년복지MOA 백엔드 서버 (Spring Boot)

## 개발 환경
- Java 21
- Spring Boot 4.0.6
- Gradle
- MyBatis
- Oracle 19c

## 시작하기

### 1. application.properties 설정
src/main/resources/application.properties.example 파일을 복사해서
src/main/resources/application.properties 로 이름 변경 후
실제 DB 정보 입력

### 2. 실행
MoaBackendApplication.java 우클릭 → Run As → Spring Boot App

## 브랜치 전략
- `main` : 배포용
- `feature/기능명` : 기능 개발

예시)
- `feature/member` : 회원 기능
- `feature/welfare` : 복지서비스 기능
- `feature/board` : 게시판 기능
- `feature/admin` : 관리자 기능

## 커밋 메시지 규칙
- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `refactor` : 코드 리팩토링
- `docs` : 문서 수정
- `chore` : 빌드, 설정 파일 수정

예시)
- `feat: 회원가입 API 구현`
- `fix: 로그인 토큰 오류 수정`
