# UMC Mission 4 - 전체 API 문서

## 엔티티 관계도

```
Member (회원)
├── Cart (1:1) - 장바구니
│   └── CartItem (1:N) - 장바구니 아이템
│       └── BookItem (N:1)
├── Order (1:N) - 주문
│   └── OrderItem (1:N) - 주문 아이템
│       └── BookItem (N:1)
└── Review (1:N) - 리뷰
    └── Book (N:1)

Book (도서)
├── BookItem (1:N) - 도서 재고/판매 단위
└── Review (1:N)
```

---

## 1. 회원 (Member) API

### POST /api/members - 회원 가입
**요청 본문:**
```json
{
  "email": "user@example.com",
  "status": "ACTIVE"
}
```
- status: ACTIVE, INACTIVE, BANNED, DELETED

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "status": "ACTIVE"
  }
}
```

### GET /api/members/{id} - 회원 조회
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "status": "ACTIVE"
  }
}
```

### PUT /api/members/{id} - 회원 정보 수정
**요청 본문:**
```json
{
  "status": "INACTIVE"
}
```

### DELETE /api/members/{id} - 회원 삭제
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "회원이 성공적으로 삭제되었습니다.",
  "data": null
}
```

---

## 2. 도서 (Book) API

### POST /api/books - 도서 생성 (BookItem 포함)
**요청 본문:**
```json
{
  "title": "클린 코드",
  "author": "로버트 C. 마틴",
  "description": "소프트웨어 장인 정신",
  "bookItems": [
    {
      "isbn": "978-8966260959",
      "price": 33000,
      "quantity": 100
    },
    {
      "isbn": "978-8966260960",
      "price": 35000,
      "quantity": 50
    }
  ]
}
```

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 1,
    "title": "클린 코드",
    "author": "로버트 C. 마틴",
    "description": "소프트웨어 장인 정신",
    "bookItems": [
      {
        "id": 1,
        "isbn": "978-8966260959",
        "price": 33000,
        "quantity": 100
      },
      {
        "id": 2,
        "isbn": "978-8966260960",
        "price": 35000,
        "quantity": 50
      }
    ]
  }
}
```

### GET /api/books/{id} - 도서 조회
도서 정보와 함께 모든 BookItem도 반환됩니다.

### DELETE /api/books/{id} - 도서 삭제
도서와 연관된 모든 BookItem도 cascade 삭제됩니다.

---

## 3. 도서 아이템 (BookItem) API

### POST /api/book-items - 도서 아이템 생성
기존 도서에 새로운 판매 단위를 추가합니다 (전자책, 종이책, 출판연도별 등).

**요청 본문:**
```json
{
  "bookId": 1,
  "isbn": "979-11-6521-234-5",
  "price": 28000,
  "quantity": 50
}
```

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 3,
    "isbn": "979-11-6521-234-5",
    "price": 28000,
    "quantity": 50
  }
}
```

### GET /api/book-items/{id} - 도서 아이템 조회
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 3,
    "isbn": "979-11-6521-234-5",
    "price": 28000,
    "quantity": 50
  }
}
```

### PUT /api/book-items/{id} - 도서 아이템 수정
**요청 본문:**
```json
{
  "price": 25000,
  "quantity": 45
}
```

### DELETE /api/book-items/{id} - 도서 아이템 삭제
⚠️ **중요:** BookItem 삭제 후 해당 Book이 더 이상 BookItem을 가지지 않으면, Book도 자동으로 삭제됩니다.

**사용 예시:**
- Book에 종이책과 전자책 2개의 BookItem이 있음
- 종이책 BookItem 삭제 → Book은 유지됨 (전자책이 남아있음)
- 전자책 BookItem도 삭제 → Book도 자동 삭제됨 ✅

---

## 4. 장바구니 (Cart) API

### POST /api/carts/items - 장바구니에 아이템 추가
**요청 본문:**
```json
{
  "memberId": 1,
  "bookItemId": 1,
  "quantity": 2
}
```

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 1,
    "bookItemId": 1,
    "quantity": 2,
    "totalPrice": 66000
  }
}
```

### GET /api/carts/items/{id} - 장바구니 아이템 조회
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "bookItemId": 1,
    "quantity": 2,
    "totalPrice": 66000
  }
}
```

### DELETE /api/carts/items/{id} - 장바구니 아이템 삭제
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "장바구니 아이템이 성공적으로 삭제되었습니다.",
  "data": null
}
```

---

## 5. 주문 (Order) API

### POST /api/orders - 주문 생성
**요청 본문:**
```json
{
  "memberId": 1,
  "orderItems": [
    {
      "bookItemId": 1,
      "quantity": 2
    },
    {
      "bookItemId": 2,
      "quantity": 1
    }
  ]
}
```

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "totalPrice": 101000,
    "items": [
      {
        "id": 1,
        "bookItemId": 1,
        "price": 33000,
        "quantity": 2
      },
      {
        "id": 2,
        "bookItemId": 2,
        "price": 35000,
        "quantity": 1
      }
    ]
  }
}
```

### GET /api/orders/{id} - 주문 조회
주문 정보와 함께 모든 OrderItem도 반환됩니다.

---

## 6. 리뷰 (Review) API

### POST /api/reviews - 리뷰 생성
**요청 본문:**
```json
{
  "memberId": 1,
  "bookId": 1,
  "rating": 5,
  "content": "정말 좋은 책입니다. 모든 개발자가 읽어야 할 필독서!"
}
```

**응답:**
```json
{
  "code": "COMMON201_1",
  "message": "리소스가 생성되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "bookId": 1,
    "rating": 5,
    "content": "정말 좋은 책입니다. 모든 개발자가 읽어야 할 필독서!"
  }
}
```

### GET /api/reviews/{id} - 리뷰 조회
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    "id": 1,
    "memberId": 1,
    "bookId": 1,
    "rating": 5,
    "content": "정말 좋은 책입니다. 모든 개발자가 읽어야 할 필독서!"
  }
}
```

### PUT /api/reviews/{id} - 리뷰 수정
**요청 본문:**
```json
{
  "memberId": 1,
  "bookId": 1,
  "rating": 4,
  "content": "좋은 책이지만 일부 내용이 어렵습니다."
}
```

### DELETE /api/reviews/{id} - 리뷰 삭제
**응답:**
```json
{
  "code": "COMMON200_1",
  "message": "리뷰가 성공적으로 삭제되었습니다.",
  "data": null
}
```

---

## 주요 특징

### 1. 관계형 데이터 구조
- **Book → BookItem (1:N)**: 한 도서는 여러 판매 단위(ISBN, 가격, 재고)를 가질 수 있음
- **Member → Cart (1:1)**: 회원당 하나의 장바구니
- **Cart → CartItem (1:N)**: 장바구니는 여러 아이템 보유
- **Member → Order (1:N)**: 회원은 여러 주문 가능
- **Order → OrderItem (1:N)**: 주문은 여러 아이템 포함

### 2. Cascade 설정
- Book 삭제 시 → BookItem 자동 삭제
- Cart 삭제 시 → CartItem 자동 삭제
- Order 삭제 시 → OrderItem 자동 삭제

### 3. 검증 (Validation)
- 모든 DTO에 `@Valid` 어노테이션 적용
- 엔티티 레벨에서도 제약조건 설정
- 커스텀 에러 메시지 제공

### 4. JPA Auditing
- 모든 엔티티는 `BaseEntity` 상속
- `createdAt`, `updatedAt` 자동 관리

### 5. 통합 응답 형식
```json
{
  "code": "상태코드",
  "message": "메시지",
  "data": { ... }
}
```

---

## Swagger UI 접속

애플리케이션 실행 후:
```
http://localhost:8081/swagger-ui/index.html
```

모든 API를 테스트하고 상세 스키마를 확인할 수 있습니다.

---

## 예외 상황 처리

### 404 Not Found
```json
{
  "code": "COMMON404_1",
  "message": "BookItem not found"
}
```

### 400 Bad Request (Validation Error)
```json
{
  "code": "COMMON400_1",
  "message": "제목은 필수입니다."
}
```

### 500 Internal Server Error
```json
{
  "code": "COMMON500_1",
  "message": "서버 오류가 발생했습니다."
}
```

