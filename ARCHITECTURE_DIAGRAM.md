# Spring Security 인증 아키텍처 다이어그램

## 시스템 구조

```
┌─────────────────────────────────────────────────────────────────┐
│                         클라이언트                               │
│                    (Postman, Browser, etc)                       │
└───────────┬─────────────────────────────────────┬───────────────┘
            │                                     │
            │ JWT 방식                           │ 세션 방식
            │ (Bearer Token)                     │ (Cookie)
            │                                     │
┌───────────▼─────────────┐       ┌──────────────▼──────────────┐
│   JWT 인증 경로          │       │   세션 인증 경로             │
│   /api/jwt/**           │       │   /api/session/**           │
└───────────┬─────────────┘       └──────────────┬──────────────┘
            │                                     │
┌───────────▼─────────────────────────────────────▼───────────────┐
│              Spring Security Filter Chain                        │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SecurityFilterChain #1 (JWT) - Order(1)                  │  │
│  │  - /api/jwt/** 경로 매칭                                 │  │
│  │  - JwtAuthenticationFilter 적용                          │  │
│  │  - SessionCreationPolicy.STATELESS                       │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SecurityFilterChain #2 (Session) - Order(2)              │  │
│  │  - /api/session/** 경로 매칭                             │  │
│  │  - Form Login 처리                                       │  │
│  │  - SessionCreationPolicy.IF_REQUIRED                     │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SecurityFilterChain #3 (Default) - Order(3)              │  │
│  │  - Swagger, H2 Console 허용                              │  │
│  │  - /api/members/** 허용                                  │  │
│  └──────────────────────────────────────────────────────────┘  │
└──────────────────────────────┬───────────────────────────────────┘
                               │
                ┌──────────────▼──────────────┐
                │  CustomUserDetailsService   │
                │  - loadUserByUsername()     │
                │  - DB에서 사용자 정보 로드   │
                └──────────────┬──────────────┘
                               │
                ┌──────────────▼──────────────┐
                │    MemberRepository         │
                │    - findByEmail()          │
                └──────────────┬──────────────┘
                               │
                ┌──────────────▼──────────────┐
                │       H2 Database           │
                │    (Member 테이블)           │
                └─────────────────────────────┘
```

---

## JWT 인증 플로우

### 1. 회원가입 & 로그인
```
┌─────────┐      POST /api/jwt/auth/register      ┌──────────────┐
│         │  ──────────────────────────────────►  │              │
│ Client  │   { email, password }                 │ JwtAuth      │
│         │                                        │ Controller   │
│         │  ◄──────────────────────────────────  │              │
└─────────┘   { accessToken, memberId, ... }      └──────┬───────┘
                                                          │
                                                          │
                                                   ┌──────▼───────┐
                                                   │ JwtToken     │
                                                   │ Provider     │
                                                   │ - generate() │
                                                   └──────────────┘
```

### 2. 인증된 요청
```
┌─────────┐   GET /api/jwt/auth/me                ┌──────────────┐
│         │   Header: Authorization: Bearer XXX   │              │
│ Client  │  ──────────────────────────────────►  │ Jwt          │
│         │                                        │ Authentication
│         │                                        │ Filter       │
└─────────┘                                        └──────┬───────┘
                                                          │
                                     ┌────────────────────▼────────┐
                                     │ 1. Extract Token            │
                                     │ 2. Validate Token           │
                                     │ 3. Get User from Token      │
                                     │ 4. Set SecurityContext      │
                                     └────────────┬────────────────┘
                                                  │
                                     ┌────────────▼────────────────┐
                                     │ SecurityContext             │
                                     │ - Authentication 정보 저장   │
                                     └─────────────────────────────┘
```

---

## 세션 인증 플로우

### 1. 로그인
```
┌─────────┐   POST /api/session/auth/login        ┌──────────────┐
│         │   email=user@example.com               │              │
│ Client  │   password=password123                 │ Form Login   │
│         │  ──────────────────────────────────►  │ Handler      │
│         │                                        │              │
│         │  ◄──────────────────────────────────  │              │
└─────────┘   Set-Cookie: JSESSIONID=XXX          └──────┬───────┘
                                                          │
                                                   ┌──────▼───────┐
                                                   │ Session      │
                                                   │ Repository   │
                                                   │ (서버 메모리)  │
                                                   └──────────────┘
```

### 2. 인증된 요청
```
┌─────────┐   GET /api/session/auth/me            ┌──────────────┐
│         │   Cookie: JSESSIONID=XXX               │              │
│ Client  │  ──────────────────────────────────►  │ Session      │
│         │                                        │ Management   │
│         │  ◄──────────────────────────────────  │ Filter       │
└─────────┘   { memberId, email, ... }            └──────┬───────┘
                                                          │
                                     ┌────────────────────▼────────┐
                                     │ 1. Extract JSESSIONID       │
                                     │ 2. Load Session from Store  │
                                     │ 3. Get Authentication       │
                                     │ 4. Set SecurityContext      │
                                     └─────────────────────────────┘
```

---

## 컴포넌트 상세 설명

### 1. JwtTokenProvider
```java
📦 역할: JWT 토큰 생성 및 검증
┣━ generateToken()        // 인증 정보로 토큰 생성
┣━ generateTokenFromEmail() // 이메일로 토큰 생성
┣━ getEmailFromToken()    // 토큰에서 이메일 추출
┗━ validateToken()        // 토큰 유효성 검증
```

### 2. JwtAuthenticationFilter
```java
📦 역할: JWT 토큰 추출 및 인증 처리
┣━ doFilterInternal()     // 필터 메인 로직
┣━ getJwtFromRequest()    // Authorization 헤더에서 토큰 추출
┗━ SecurityContext에 인증 정보 설정
```

### 3. CustomUserDetailsService
```java
📦 역할: 사용자 정보 로드
┣━ loadUserByUsername()   // 이메일로 사용자 조회
┗━ UserDetails 객체 반환
```

### 4. SecurityConfig
```java
📦 역할: 보안 설정 통합 관리
┣━ jwtFilterChain()       // JWT 인증 설정
┣━ sessionFilterChain()   // 세션 인증 설정
┣━ defaultFilterChain()   // 공개 경로 설정
┣━ passwordEncoder()      // 비밀번호 암호화
┗━ authenticationManager() // 인증 관리자
```

---

## 경로별 접근 제어

| 경로 패턴 | 인증 방식 | 접근 권한 | 설명 |
|----------|----------|---------|------|
| `/api/jwt/auth/**` | - | 공개 | JWT 회원가입/로그인 |
| `/api/jwt/**` | JWT | 인증 필요 | JWT 보호 리소스 |
| `/api/session/auth/**` | - | 공개 | 세션 회원가입/로그인 |
| `/api/session/**` | 세션 | 인증 필요 | 세션 보호 리소스 |
| `/api/members/**` | - | 공개 | 기존 회원 API |
| `/swagger-ui/**` | - | 공개 | Swagger UI |
| `/h2-console/**` | - | 공개 | H2 콘솔 |

---

## 데이터베이스 스키마

```sql
CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,     -- BCrypt 암호화
    status VARCHAR(20) NOT NULL,        -- ACTIVE, INACTIVE, BANNED, DELETED
    role VARCHAR(20) NOT NULL,          -- ROLE_USER, ROLE_ADMIN
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

---

## 보안 흐름 요약

### JWT 방식
1. 클라이언트가 로그인
2. 서버가 JWT 토큰 발급
3. 클라이언트가 토큰을 로컬 스토리지에 저장
4. 매 요청마다 Authorization 헤더에 토큰 포함
5. 서버가 토큰 검증 후 요청 처리

### 세션 방식
1. 클라이언트가 로그인
2. 서버가 세션 생성 및 JSESSIONID 쿠키 발급
3. 브라우저가 쿠키 자동 저장
4. 매 요청마다 쿠키 자동 전송
5. 서버가 세션 확인 후 요청 처리

---

## 설정 값

```properties
# JWT 설정
jwt.secret=mySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256AlgorithmSecurityPurpose
jwt.expiration=86400000  # 24시간 (밀리초)

# 세션 설정 (Spring Boot 기본값)
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false  # HTTPS 사용 시 true
```

---

## 테스트 시나리오

### ✅ JWT 테스트
1. 회원가입 → 토큰 발급 확인
2. 로그인 → 토큰 발급 확인
3. 토큰으로 /api/jwt/auth/me 호출 → 200 OK
4. 잘못된 토큰으로 호출 → 401 Unauthorized
5. 토큰 없이 호출 → 401 Unauthorized

### ✅ 세션 테스트
1. 회원가입 → 회원 정보 반환
2. 로그인 → JSESSIONID 쿠키 확인
3. 쿠키로 /api/session/auth/me 호출 → 200 OK
4. 로그아웃 → 세션 삭제 확인
5. 로그아웃 후 재호출 → 401 Unauthorized

---

이 다이어그램은 프로젝트의 인증 아키텍처를 시각화한 것입니다.
각 컴포넌트의 역할과 상호작용을 이해하는 데 도움이 됩니다.

