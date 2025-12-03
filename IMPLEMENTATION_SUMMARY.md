# Spring Security JWT & Session 인증 구현 완료

## 🎯 구현 완료 사항

### 1. 엔티티 및 DTO 업데이트
✅ **Member 엔티티**에 인증 필드 추가:
- `password`: BCrypt 암호화 비밀번호 필드
- `role`: 권한 관리 필드 (ROLE_USER, ROLE_ADMIN)

✅ **새로운 DTO 생성**:
- `LoginRequest.java`: 로그인 요청 DTO
- `RegisterRequest.java`: 회원가입 요청 DTO  
- `AuthResponse.java`: 인증 응답 DTO (토큰 포함)
- `MemberDto.java`: password, role 필드 추가

### 2. Spring Security 설정
✅ **SecurityConfig.java** - 3개의 SecurityFilterChain 구성:
1. **JWT FilterChain** (`/api/jwt/**`)
   - Stateless 세션 정책
   - JWT 인증 필터 적용
   - Bearer 토큰 방식
   
2. **Session FilterChain** (`/api/session/**`)
   - 세션 기반 인증
   - Form 로그인 지원
   - 동시 세션 1개 제한
   
3. **Default FilterChain** 
   - Swagger UI, H2 Console 접근 허용
   - 기존 `/api/members/**` 접근 허용

### 3. JWT 인증 컴포넌트
✅ **JwtTokenProvider.java**
- JWT 토큰 생성 및 검증
- JJWT 라이브러리 (v0.12.3) 사용
- HS256 알고리즘
- 24시간 만료 시간

✅ **JwtAuthenticationFilter.java**
- OncePerRequestFilter 상속
- Authorization 헤더에서 Bearer 토큰 추출
- 유효한 토큰 시 SecurityContext에 인증 정보 설정

✅ **CustomUserDetailsService.java**
- UserDetailsService 구현
- 데이터베이스에서 사용자 로드
- 권한 정보 매핑

### 4. 인증 컨트롤러
✅ **JwtAuthController.java** (`/api/jwt/auth`)
- POST `/register` - JWT 회원가입
- POST `/login` - JWT 로그인 (토큰 발급)
- GET `/me` - 현재 사용자 정보 조회

✅ **SessionAuthController.java** (`/api/session/auth`)
- POST `/register` - 세션 회원가입
- POST `/login` - 세션 로그인 (Form 기반)
- POST `/logout` - 로그아웃
- GET `/check` - 세션 유효성 확인
- GET `/me` - 현재 사용자 정보 조회

### 5. 의존성 추가
✅ **build.gradle**에 추가:
```groovy
// Spring Security
implementation 'org.springframework.boot:spring-boot-starter-security'
testImplementation 'org.springframework.security:spring-security-test'

// JWT (JJWT 0.12.3)
implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'
```

### 6. 설정 파일
✅ **application.properties**에 추가:
```properties
jwt.secret=mySecretKeyForJWTTokenGenerationMustBeLongEnoughForHS256AlgorithmSecurityPurpose
jwt.expiration=86400000  # 24시간
```

### 7. Swagger 문서화
✅ 모든 인증 API에 한국어 설명 추가:
- Operation 어노테이션으로 상세 설명
- 태그로 API 그룹화 (JWT 인증 API, 세션 인증 API)

---

## 📁 생성된 파일 목록

### Config 폴더
```
src/main/java/com/hexter31376/umc_mission4/config/
├── SecurityConfig.java                    # Spring Security 설정
└── security/
    ├── JwtTokenProvider.java              # JWT 토큰 생성/검증
    ├── JwtAuthenticationFilter.java       # JWT 인증 필터
    └── CustomUserDetailsService.java      # 사용자 정보 로드
```

### Controller 폴더
```
src/main/java/com/hexter31376/umc_mission4/controller/auth/
├── JwtAuthController.java                 # JWT 인증 API
└── SessionAuthController.java             # 세션 인증 API
```

### DTO 폴더
```
src/main/java/com/hexter31376/umc_mission4/dto/member/
├── LoginRequest.java                      # 로그인 요청
├── RegisterRequest.java                   # 회원가입 요청
├── AuthResponse.java                      # 인증 응답
└── MemberDto.java                         # 업데이트됨 (password, role 추가)
```

### 문서
```
SECURITY_AUTH_GUIDE.md                     # 사용 가이드
```

---

## 🚀 테스트 방법

### 1. 애플리케이션 실행
```bash
cd /Users/jang-won-young/projects/GitHubProjects/umc_mission4
./gradlew bootRun
```

### 2. Swagger UI 접속
```
http://localhost:8081/swagger-ui.html
```

### 3. JWT 인증 플로우
```bash
# 1. 회원가입
POST http://localhost:8081/api/jwt/auth/register
{
  "email": "test@example.com",
  "password": "password123"
}

# 2. 로그인 (토큰 발급)
POST http://localhost:8081/api/jwt/auth/login
{
  "email": "test@example.com",
  "password": "password123"
}
# 응답에서 accessToken 복사

# 3. 인증 필요 API 호출
GET http://localhost:8081/api/jwt/auth/me
Header: Authorization: Bearer {accessToken}
```

### 4. 세션 인증 플로우
```bash
# 1. 회원가입
POST http://localhost:8081/api/session/auth/register
{
  "email": "test2@example.com",
  "password": "password123"
}

# 2. 로그인 (세션 생성)
POST http://localhost:8081/api/session/auth/login
Content-Type: application/x-www-form-urlencoded
Body: email=test2@example.com&password=password123

# 3. 세션으로 API 호출 (쿠키 자동 전송)
GET http://localhost:8081/api/session/auth/me

# 4. 로그아웃
POST http://localhost:8081/api/session/auth/logout
```

---

## 🔐 보안 기능

### 비밀번호 암호화
- BCryptPasswordEncoder 사용
- 강도: 기본값 (10 rounds)

### JWT 보안
- HS256 알고리즘
- 64자 이상의 Secret Key
- 토큰 만료: 24시간
- Bearer 스킴 사용

### 세션 보안
- CSRF 비활성화 (개발용)
- 동시 세션 1개 제한
- 세션 쿠키 자동 관리

### 접근 제어
- `/api/jwt/auth/**` - 공개 (회원가입/로그인)
- `/api/jwt/**` - JWT 인증 필요
- `/api/session/auth/**` - 공개 (회원가입/로그인)
- `/api/session/**` - 세션 인증 필요
- `/swagger-ui/**` - 공개
- `/h2-console/**` - 공개

---

## ✅ 검증 완료 사항

1. ✅ Member 엔티티에 password, role 필드 추가
2. ✅ JWT 토큰 생성 및 검증 로직
3. ✅ JWT 필터 체인 구성
4. ✅ 세션 필터 체인 구성
5. ✅ CustomUserDetailsService 구현
6. ✅ PasswordEncoder 빈 등록
7. ✅ AuthenticationManager 빈 등록
8. ✅ 회원가입 API (JWT/Session)
9. ✅ 로그인 API (JWT/Session)
10. ✅ 현재 사용자 조회 API
11. ✅ Swagger 문서화
12. ✅ 의존성 추가 (JJWT, Spring Security)
13. ✅ application.properties 설정

---

## 📝 다음 단계 제안

### 즉시 구현 가능
- [ ] 리프레시 토큰 구현
- [ ] 비밀번호 변경 API
- [ ] 회원 탈퇴 API
- [ ] 관리자 전용 API

### 추가 기능
- [ ] 이메일 인증
- [ ] 비밀번호 찾기 (이메일 전송)
- [ ] OAuth2 소셜 로그인
- [ ] 역할 기반 세밀한 권한 제어
- [ ] 로그인 시도 제한 (브루트포스 방지)
- [ ] JWT 블랙리스트 (로그아웃 토큰 무효화)

---

## 🎉 완료!

Spring Security를 사용한 **JWT 기반 인증**과 **세션 기반 인증** 모두 구현이 완료되었습니다!

이제 다음 명령으로 애플리케이션을 실행하고 테스트할 수 있습니다:
```bash
./gradlew bootRun
```

Swagger UI에서 모든 API를 테스트할 수 있습니다:
```
http://localhost:8081/swagger-ui.html
```

