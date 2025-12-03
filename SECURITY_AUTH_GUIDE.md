# Spring Security 인증 구현 가이드

## 개요
이 프로젝트는 **세션 기반 인증**과 **JWT 기반 인증** 두 가지 방식을 모두 지원합니다.

## 구현된 기능

### 1. 세션 기반 인증 (Session-based Authentication)
- **경로**: `/api/session/**`
- **특징**: 
  - 서버에서 세션을 관리
  - 브라우저 쿠키를 통한 자동 인증
  - 동시 세션 1개로 제한

### 2. JWT 기반 인증 (JWT Token Authentication)
- **경로**: `/api/jwt/**`
- **특징**:
  - Stateless 인증 (서버에 세션 저장 안 함)
  - Authorization 헤더에 Bearer 토큰 포함
  - 토큰 만료 시간: 24시간

## API 엔드포인트

### JWT 인증 API

#### 1. 회원가입 (POST /api/jwt/auth/register)
```json
{
  "email": "user@example.com",
  "password": "password123",
  "status": "ACTIVE"
}
```

**응답:**
```json
{
  "code": "CREATED",
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "memberId": 1,
    "email": "user@example.com",
    "role": "ROLE_USER"
  }
}
```

#### 2. 로그인 (POST /api/jwt/auth/login)
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**응답:**
```json
{
  "code": "OK",
  "message": "로그인에 성공했습니다.",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "memberId": 1,
    "email": "user@example.com",
    "role": "ROLE_USER"
  }
}
```

#### 3. 현재 사용자 정보 조회 (GET /api/jwt/auth/me)
**헤더:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

**응답:**
```json
{
  "code": "OK",
  "message": "사용자 정보 조회 성공",
  "data": {
    "memberId": 1,
    "email": "user@example.com",
    "role": "ROLE_USER"
  }
}
```

### 세션 인증 API

#### 1. 회원가입 (POST /api/session/auth/register)
```json
{
  "email": "user@example.com",
  "password": "password123",
  "status": "ACTIVE"
}
```

#### 2. 로그인 (POST /api/session/auth/login)
**Form Data:**
```
email=user@example.com
password=password123
```

**또는 JSON:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**응답:**
```json
{
  "message": "세션 로그인 성공"
}
```

#### 3. 로그아웃 (POST /api/session/auth/logout)
**응답:**
```json
{
  "message": "로그아웃 성공"
}
```

#### 4. 세션 확인 (GET /api/session/auth/check)
**응답:**
```json
{
  "code": "OK",
  "message": "세션이 유효합니다.",
  "data": "로그인된 사용자: user@example.com"
}
```

#### 5. 현재 사용자 정보 조회 (GET /api/session/auth/me)
**응답:**
```json
{
  "code": "OK",
  "message": "사용자 정보 조회 성공",
  "data": {
    "memberId": 1,
    "email": "user@example.com",
    "role": "ROLE_USER"
  }
}
```

## 테스트 방법

### 1. JWT 인증 테스트 (Postman/cURL)

```bash
# 회원가입
curl -X POST http://localhost:8081/api/jwt/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# 로그인
curl -X POST http://localhost:8081/api/jwt/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# 인증이 필요한 API 호출
curl -X GET http://localhost:8081/api/jwt/auth/me \
  -H "Authorization: Bearer {받은_토큰}"
```

### 2. 세션 인증 테스트 (브라우저/Postman)

```bash
# 회원가입
curl -X POST http://localhost:8081/api/session/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# 로그인 (쿠키 저장)
curl -X POST http://localhost:8081/api/session/auth/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "email=test@example.com&password=password123" \
  -c cookies.txt

# 세션을 사용한 API 호출
curl -X GET http://localhost:8081/api/session/auth/me \
  -b cookies.txt

# 로그아웃
curl -X POST http://localhost:8081/api/session/auth/logout \
  -b cookies.txt
```

## 보안 설정

### SecurityConfig.java 구조
```java
- jwtFilterChain (@Order(1)): /api/jwt/** 경로 처리
- sessionFilterChain (@Order(2)): /api/session/** 경로 처리
- defaultFilterChain (@Order(3)): Swagger, H2 Console 등 공개 경로
```

### 권한 설정
- `ROLE_USER`: 일반 사용자
- `ROLE_ADMIN`: 관리자 (향후 확장용)

## 주요 컴포넌트

### 1. JwtTokenProvider
- JWT 토큰 생성 및 검증
- 토큰에서 사용자 정보 추출

### 2. JwtAuthenticationFilter
- HTTP 요청에서 JWT 토큰 추출
- 토큰 검증 후 SecurityContext에 인증 정보 설정

### 3. CustomUserDetailsService
- Spring Security의 UserDetailsService 구현
- 데이터베이스에서 사용자 정보 로드

### 4. SecurityConfig
- 세 개의 SecurityFilterChain 설정
- JWT와 세션 인증을 각각 다른 경로에 적용

## 설정 파일 (application.properties)

```properties
# JWT 설정
jwt.secret=mySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256AlgorithmSecurityPurpose
jwt.expiration=86400000  # 24시간 (밀리초)
```

## 엔티티 변경사항

### Member 엔티티에 추가된 필드:
```java
private String password;  // 암호화된 비밀번호
private String role;      // 권한 (ROLE_USER, ROLE_ADMIN)
```

## Swagger UI 접속
- URL: http://localhost:8081/swagger-ui.html
- JWT 인증 API와 세션 인증 API가 각각 태그로 분리되어 있습니다.

## 주의사항
1. **비밀번호**: BCrypt로 암호화되어 저장됩니다.
2. **JWT Secret**: 운영 환경에서는 환경 변수로 관리해야 합니다.
3. **CSRF**: 개발 편의를 위해 비활성화되어 있습니다. 운영 환경에서는 활성화를 권장합니다.
4. **세션 동시성**: 현재 1개 세션만 허용됩니다. `maximumSessions`로 조정 가능합니다.

## 다음 단계
- [ ] 리프레시 토큰 구현
- [ ] 비밀번호 찾기/변경 기능
- [ ] 이메일 인증
- [ ] OAuth2 소셜 로그인
- [ ] 역할 기반 접근 제어(RBAC) 강화

