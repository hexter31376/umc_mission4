Mission: Global error/success handling and notes on RestControllerAdvice

1) 목적
- 전역 예외 처리 및 응답 포맷 통일
- 성공 / 실패 응답에 대한 표준 코드와 메시지 정의

2) `RestControllerAdvice`의 장점
- 중앙 집중식 예외 처리: 예외 처리 로직이 컨트롤러마다 분산되지 않고 한 곳에 모여 있어 유지보수성이 좋아집니다.
- 응답 일관성: 예외 발생 시 일관된 형식(예: {code, message})으로 클라이언트에 반환할 수 있어 API 소비자가 처리가 쉽습니다.
- 중복 제거: 각 컨트롤러에서 동일한 예외-응답 매핑을 여러 번 구현할 필요가 없습니다.
- 예외별 세부 처리 가능: 예외 타입에 따라 다른 HTTP 상태 코드 및 메시지를 반환할 수 있습니다.
- 전역 로깅 / 모니터링 연결: 예외 발생 시 중앙에서 로깅, 알림, 메트릭 수집을 일원화할 수 있습니다.

3) `RestControllerAdvice`가 없는 경우 불편한 점
- 중복 코드 증가: 각 컨트롤러에 try-catch 또는 @ExceptionHandler를 반복 구현해야 합니다.
- 불일치한 응답 포맷: 각 컨트롤러가 서로 다른 에러 포맷/상태 코드를 반환할 수 있어 클라이언트가 처리하기 어려움.
- 유지보수 비용 증가: 예외 처리 정책 변경 시 모든 컨트롤러를 수정해야 할 수 있습니다.
- 누락된 예외 처리: 일부 컨트롤러에서 예외 처리를 빠뜨리면 의도하지 않은 500 에러나 민감한 내부 메시지가 노출될 수 있음.

4) 이번 작업에서 적용한 응답 통일 방식
- 성공 응답: `ApiSuccessResponse<T>`(code, message, data)
  - 코드/메시지는 `GeneralSuccessCode` 열거형으로 관리 (OK, CREATED 등)
- 실패 응답: `ApiErrorResponse`(code, message)
  - 코드/메시지는 `GeneralErrorCode` 열거형으로 관리 (BAD_REQUEST, NOT_FOUND, ...)
- 전역 핸들러: `GlobalExceptionHandler`를 통해 아래 예외들을 매핑
  - IllegalArgumentException -> BAD_REQUEST
  - MethodArgumentNotValidException -> BAD_REQUEST (필드별 메시지 포함)
  - EntityNotFoundException -> NOT_FOUND
  - Exception -> INTERNAL_ERROR

5) 파일 목록 (주요 변경)
- `global/apiPayload/ApiSuccessResponse.java` (성공 응답 포맷)
- `global/apiPayload/code/GeneralSuccessCode.java` (성공 코드)
- `global/exception/GlobalExceptionHandler.java` (validation 메시지 개선 및 통일된 에러 반환)
- 각 컨트롤러에서 ApiSuccessResponse로 응답 변경

6) 권장 개선 사항 (후속 작업)
- 더 세부적인 성공/실패 코드 추가 (예: MEMBER_CREATED, BOOK_CREATED 등)
- API 문서화 (OpenAPI/Swagger)와 연동하여 code, message를 문서화
- 통합 테스트: 모든 엔드포인트에 대해 성공/실패 케이스 테스트 추가
- 국제화: 클라이언트 로케일에 따라 메시지 i18n 적용

작성자: 자동 생성 (pair-programmer style)
작성일: 2025-11-05

