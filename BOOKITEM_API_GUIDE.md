# BookItem API 사용 가이드

## 🎯 BookItem이란?

BookItem은 **도서의 실제 판매 단위**를 나타냅니다. 하나의 도서(Book)는 여러 판매 형태를 가질 수 있습니다:

- 📱 **전자책** (E-book)
- 📖 **종이책** (Paperback)
- 📚 **하드커버** (Hardcover)
- 📅 **다른 출판 연도** (2023년판, 2024년판 등)
- 🌍 **다른 언어 버전** (한국어판, 영어판 등)

각 BookItem은 고유한 ISBN, 가격, 재고를 가집니다.

---

## 📋 API 엔드포인트

### 1. BookItem 생성
**POST** `/api/book-items`

기존 도서에 새로운 판매 단위를 추가합니다.

**요청 예시:**
```json
{
  "bookId": 1,
  "isbn": "979-11-6521-234-5",
  "price": 28000,
  "quantity": 50
}
```

**응답 예시:**
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

---

### 2. BookItem 조회
**GET** `/api/book-items/{id}`

**응답 예시:**
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

---

### 3. BookItem 수정
**PUT** `/api/book-items/{id}`

가격과 재고 수량만 수정 가능합니다. (ISBN과 연결된 Book은 변경 불가)

**요청 예시:**
```json
{
  "price": 25000,
  "quantity": 45
}
```

**응답 예시:**
```json
{
  "code": "COMMON200_1",
  "message": "도서 아이템이 성공적으로 수정되었습니다.",
  "data": {
    "id": 3,
    "isbn": "979-11-6521-234-5",
    "price": 25000,
    "quantity": 45
  }
}
```

---

### 4. BookItem 삭제 ⚠️ 중요!
**DELETE** `/api/book-items/{id}`

BookItem을 삭제합니다. 

**⚠️ 자동 Book 삭제 기능:**
- BookItem 삭제 후, 해당 Book이 더 이상 어떤 BookItem도 가지지 않으면
- **Book 자체도 자동으로 삭제됩니다**
- 예: 전자책과 종이책이 모두 삭제되면 도서 자체가 삭제됨

**응답 예시:**
```json
{
  "code": "COMMON200_1",
  "message": "도서 아이템이 성공적으로 삭제되었습니다.",
  "data": null
}
```

---

## 💡 실제 사용 시나리오

### 시나리오 1: 전자책과 종이책을 함께 판매

```bash
# 1. 도서 생성 (종이책과 함께)
POST /api/books
{
  "title": "클린 코드",
  "author": "로버트 C. 마틴",
  "description": "소프트웨어 장인 정신",
  "bookItems": [
    {
      "isbn": "978-89-6626-095-9",
      "price": 33000,
      "quantity": 100
    }
  ]
}

# 2. 나중에 전자책 버전 추가
POST /api/book-items
{
  "bookId": 1,
  "isbn": "979-11-6521-234-5",
  "price": 23000,
  "quantity": 999
}

# 결과: 같은 도서에 종이책(33,000원)과 전자책(23,000원) 두 가지 판매 단위 존재
```

---

### 시나리오 2: 2024년판 추가

```bash
# 기존: 2023년판
# BookItem ID: 1, ISBN: 978-89-6626-095-9

# 2024년 개정판 추가
POST /api/book-items
{
  "bookId": 1,
  "isbn": "978-89-6626-099-7",
  "price": 35000,
  "quantity": 50
}

# 결과: 같은 도서에 2023년판과 2024년판 두 가지 존재
```

---

### 시나리오 3: 재고 관리

```bash
# 재고 업데이트 (할인 및 재고 조정)
PUT /api/book-items/1
{
  "price": 29700,
  "quantity": 80
}

# 결과: 가격이 33,000원 → 29,700원으로 변경, 재고 100개 → 80개로 조정
```

---

### 시나리오 4: 품절된 BookItem 삭제

```bash
# 전자책 버전 삭제
DELETE /api/book-items/2

# 만약 이것이 마지막 BookItem이었다면:
# → BookItem 삭제됨
# → Book에 더 이상 BookItem이 없음을 확인
# → Book도 자동으로 삭제됨 ✅
```

---

## 🔄 전체 워크플로우 예시

### 도서 판매 전체 과정

```bash
# 1️⃣ 도서 등록 (종이책)
POST /api/books
{
  "title": "이펙티브 자바",
  "author": "조슈아 블로크",
  "description": "자바 프로그래밍의 바이블",
  "bookItems": [
    {
      "isbn": "978-89-6626-284-7",
      "price": 36000,
      "quantity": 200
    }
  ]
}
# 응답: Book ID: 5, BookItem ID: 10

# 2️⃣ 전자책 버전 추가
POST /api/book-items
{
  "bookId": 5,
  "isbn": "979-11-6521-999-1",
  "price": 25000,
  "quantity": 999
}
# 응답: BookItem ID: 11

# 3️⃣ 영문판 추가
POST /api/book-items
{
  "bookId": 5,
  "isbn": "978-0-134-68599-1",
  "price": 50000,
  "quantity": 30
}
# 응답: BookItem ID: 12

# 현재 상태: Book(5)는 3개의 BookItem을 가짐
# - BookItem(10): 한국어 종이책 36,000원
# - BookItem(11): 한국어 전자책 25,000원
# - BookItem(12): 영문판 50,000원

# 4️⃣ 영문판이 품절되어 삭제
DELETE /api/book-items/12
# Book(5)는 여전히 2개의 BookItem을 가지므로 유지됨

# 5️⃣ 전자책도 삭제
DELETE /api/book-items/11
# Book(5)는 여전히 1개의 BookItem을 가지므로 유지됨

# 6️⃣ 종이책도 삭제
DELETE /api/book-items/10
# ⚠️ Book(5)가 더 이상 BookItem을 가지지 않음
# → Book(5)도 자동으로 삭제됨!
```

---

## ⚙️ 기술 구현 세부사항

### 자동 Book 삭제 로직

```java
public void delete(Long id) {
    // 1. BookItem 조회
    BookItem bookItem = bookItemRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BookItem not found"));
    
    // 2. Book ID 저장
    Long bookId = bookItem.getBook().getId();
    
    // 3. BookItem 삭제
    bookItemRepository.deleteById(id);
    
    // 4. Book에 남은 BookItem이 있는지 확인
    Book book = bookRepository.findById(bookId).orElse(null);
    if (book != null && bookItemRepository.countByBookId(bookId) == 0) {
        // 5. 남은 BookItem이 없으면 Book도 삭제
        bookRepository.delete(book);
    }
}
```

### 쿼리 실행 순서

1. `SELECT * FROM book_items WHERE id = ?` (BookItem 조회)
2. `DELETE FROM book_items WHERE id = ?` (BookItem 삭제)
3. `SELECT COUNT(*) FROM book_items WHERE book_id = ?` (남은 BookItem 개수 확인)
4. `DELETE FROM books WHERE id = ?` (조건 충족 시 Book 삭제)

---

## 🎯 주요 장점

1. **유연한 판매 전략**
   - 동일 도서를 다양한 형태로 판매 가능
   - 각 형태별로 독립적인 가격/재고 관리

2. **자동 정리 기능**
   - 품절된 BookItem 삭제 시 Book 자동 정리
   - 데이터베이스 무결성 유지

3. **실시간 재고 관리**
   - BookItem 단위로 재고 추적
   - 가격 업데이트 용이

4. **확장 가능한 구조**
   - 새로운 판매 형태 추가 용이
   - 기존 데이터에 영향 없음

---

## 📊 데이터 관계도

```
Book (1)
├── BookItem (1) - 종이책    [ISBN: 978-xxx, 가격: 33,000원, 재고: 100]
├── BookItem (2) - 전자책    [ISBN: 979-xxx, 가격: 23,000원, 재고: 999]
└── BookItem (3) - 하드커버  [ISBN: 980-xxx, 가격: 45,000원, 재고: 50]

만약 BookItem (1), (2), (3)을 모두 삭제하면
→ Book도 자동으로 삭제됨 ✅
```

---

## 🚀 Swagger UI에서 테스트

```
http://localhost:8081/swagger-ui/index.html
```

"도서 아이템 API" 섹션에서 모든 엔드포인트를 테스트할 수 있습니다!

