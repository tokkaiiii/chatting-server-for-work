# chatting-server

"스프링 부트로 만드는 채팅 서버" 프로젝트입니다.

## 응용프로그램 빌드

```bash
./gradlew clean build
```

## 응용프로그램 실행

```bash
./gradlew bootRun
```

## API 목록

### 회원가입

요청

- 메서드: `POST`
- URL: `/client/signup`
- 헤더: `Content-Type: application/json`
- 본문

```
CreateClientCommand {
  "email": "test@example.com",
  "username": "사용자 이름",
  "password": "비밀번호"
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/client/signup' \
-H 'Content-Type: application/json' \
-d '{
  "email: "test@example.com",
  "username": "사용자 이름",
  "password": "비밀번호"
}'
```

성공 응답

- 상태코드: `201 Created`

```json
{
  "id": "사용자 ID",
  "email": "test@example.com",
  "username": "사용자 이름",
  "createdAt": "생성일시"
}
```

정책

- 비밀번호는 8자 이상의 문자로 구성되어야 한다.

테스트

- [x] 올바르게 요청하면 `201 Created` 상태코드를 반환한다.
- [x] 사용자 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 사용자 이름 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 이메일 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 이메일 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 비밀번호 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 비밀번호 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다.
- [x] 이메일 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다.
- [x] 사용자 이름 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다.
- [x] 비밀번호를 올바르게 암호화한다.
- [x] 사용자 이름을 반환한다.
- [x] 생성일시를 반환한다.

### 사용자 토큰발행

요청

- 메서드: `POST`
- URL: `/client/issueToken`
- 헤더: `Content-Type: application/json`
- 본문

```
IssueClientTokenCommand {
  "email": "test@example.com",
  "password": "비밀번호"
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/client/issueToken' \
-H 'Content-Type: application/json' \
-d '{
  "email": "test@example.com",
  "password": "비밀번호"
}'
```

성공 응답

- 상태코드: `200
- OK`

```
AccessTokenCarrier {
  "accessToken": "사용자 토큰"
}
```

테스트

- [x] 올바르게 요청하면 `200 OK` 상태코드를 반환한다
- [x] 올바르게 요청하면 접근 토큰을 반환한다
- [x] 접근 토큰은 JWT 형식을 따른다
- [x] 존재하지 않는 이메일이 사용되면 `400 Bad Request` 상태코드를 반환한다
- [x] 잘못된 비밀번호가 사용되면 `400 Bad Request` 상태코드를 반환한다

### 로그인

요청

- 메서드: `POST`
- URL: `/client/login`
- 헤더: `Content-Type: application/json`
- 본문

```
LoginClientCommand {
  "email": "test@example.com",
  "password": "비밀번호"
}

```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/client/login' \
-H 'Content-Type: application/json' \
-d '{
  "email": "test@exmaple.com"
  "password": "비밀번호"
}'
```

성공 응답

- 상태코드: `200 OK`

```json
{
  "id": "사용자 ID",
  "username": "사용자 이름",
  "createdAt": "생성일시"
}
```

정책

- 비밀번호는 암호화되어 저장되어 있다.
- 로그인 시 사용자 이름을 반환한다.
- 로그인 시 사용자 ID는 세션에 저장된다.
- 로그인 시 사용자 이름은 세션에 저장된다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어야 한다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어 있지 않으면 `401 Unauthorized` 상태코드를 반환한다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어 있으면 `200 OK` 상태코드를 반환한다.

테스트

- [x] 올바르게 요청하면 `200 OK` 상태코드를 반환한다
- [x] 이메일 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 이메일 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 비밀번호 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 비밀번호 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 사용자 이름이 존재하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [x] 비밀번호가 일치하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [x] 로그인 시 사용자 이름을 반환한다
- [x] 로그인 시 생성일시를 반환한다

### 로그아웃

요청

- 메서드: `DELETE`
- URL: `/client/logout`
- 헤더: `Content-Type: application/json`
- 본문

```
LogoutUserCommand {
  "username": "사용자 이름"
}
```

### 사용자 정보 조회

요청

- 메서드: `GET`
- URL: `/client/me`
- 헤더

```
Authorization: Bearer {accessToken}
```

- 본문 없음
  성공 응답
- 상태코드: 200 OK
- 본문
  ```
  SellerMeView {
    id: string(UUID),
    username: string
  }
  ```

테스트

- [x] 올바르게 요청하면 `200 OK` 상태코드를 반환한다
- [x] 접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [x] 서로 다른 사용자의 식별자는 서로 다르다
- [x] 같은 사용자의 식별자는 항상 같다
- [x] 사용자의 기본 정보가 올바르게 설정된다

### 사용자 목록 조회

요청

- 메서드: `GET`
- URL: `/client/users`
- 헤더: `Content-Type: application/json`
- 본문 없음
- 성공 응답

```json
[
  {
    "id": "사용자 ID",
    "name": "사용자 이름",
    "createdAt": "생성일시"
  },
  ...
]
```

### 사용자 정보 수정

요청

- 메서드: `PUT`
- URL: `/api/users/{userId}`
- 헤더: `Content-Type: application/json`
- 경로 변수: `userId` (사용자 ID)
- 본문

```json
{
  "name": "새로운 사용자 이름",
  "password": "새로운 비밀번호"
}
```

- 성공 응답

```json
{
  "id": "사용자 ID",
  "name": "새로운 사용자 이름",
  "createdAt": "생성일시"
}
```

### 사용자 정보 삭제

요청

- 메서드: `DELETE`
- URL: `/api/users/{userId}`
- 헤더: `Content-Type: application/json`
- 경로 변수: `userId` (사용자 ID)
- 본문

```json
{
  "name": "사용자 이름",
  "password": "비밀번호"
}
```

### 관리자 토큰발행

요청

- 메서드: `POST`
- URL: `/admin/issueToken`
- 헤더: `Content-Type: application/json`
- 본문

```
IssueAdminToken {
  "email": "test@example.com",
  "password": "비밀번호"
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/admin/issueToken' \
-H 'Content-Type: application/json' \
-d '{
  "email": "test@example.com",
  "password": "비밀번호"
}'
```

성공 응답

- 상태코드: `200
- OK`

```
AccessTokenCarrier {
  "accessToken": "사용자 토큰"
}
```

테스트

- [x] 올바르게 요청하면 `200 OK` 상태코드를 반환한다
- [x] 올바르게 요청하면 접근 토큰을 반환한다
- [x] 접근 토큰은 JWT 형식을 따른다
- [x] 존재하지 않는 이메일이 사용되면 `400 Bad Request` 상태코드를 반환한다
- [x] 잘못된 비밀번호가 사용되면 `400 Bad Request` 상태코드를 반환한다

### 로그인

요청

- 메서드: `POST`
- URL: `/admin/login`
- 헤더: `Content-Type: application/json`
- 본문

```
LoginAdminCommand {
  "email": "test@example.com",
  "password": "비밀번호"
}

```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/admin/login' \
-H 'Content-Type: application/json' \
-d '{
  "email": "test@exmaple.com"
  "password": "비밀번호"
}'
```

성공 응답

- 상태코드: `200 OK`

```json
{
  "id": "사용자 ID",
  "username": "사용자 이름",
  "createdAt": "생성일시"
}
```

정책

- 비밀번호는 암호화되어 저장되어 있다.
- 로그인 시 사용자 이름을 반환한다.
- 로그인 시 사용자 ID는 세션에 저장된다.
- 로그인 시 사용자 이름은 세션에 저장된다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어야 한다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어 있지 않으면 `401 Unauthorized` 상태코드를 반환한다.
- 로그인 시 세션에 사용자 ID와 사용자 이름이 저장되어 있으면 `200 OK` 상태코드를 반환한다.

테스트

- [x] 올바르게 요청하면 `200 OK` 상태코드를 반환한다
- [x] 이메일 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 이메일 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 비밀번호 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 비밀번호 속성이 올바른 형식을 따르지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 비밀번호가 일치하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [x] 로그인 시 사용자 이름을 반환한다
- [x] 로그인 시 생성일시를 반환한다

### 채팅방 목록 조회

요청

- 메서드: `GET`
- URL: `/chat/rooms`
- 헤더: `Content-Type: application/json`
- 본문 없음
-

```
GET /chat/rooms
```

- 성공 응답

```json
{
  "id": "채팅방 ID",
  "roomName": "채팅방 이름",
  "lastMessage": "안녕하세요",
  "time": " ",
  "unread": 1
}
```

테스트

- [x] 올바르게 요청하면 `200 ok` 상태코드를 반환한다
- [x] 접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [ ] 채티방 목록을 반환한다

### 채팅방 생성

요청

- 메서드: `POST`
- URL: `/chat/rooms`
- 헤더: `Content-Type: application/json`
- 본문

```
 ChatRoomCreateCommand {
  "roomName": "채팅방 이름"
}
```

- 성공 응답

```json
{
  "id": "채팅방 ID",
  "name": "채팅방 이름",
  "createdAt": "생성일시"
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/chat/rooms' \
-H 'Content-Type: application/json' \
-d '{
  "roomName": "채팅방 이름"
}'
```

테스트

- [x] 올바르게 요청하면 `201 Created` 상태코드를 반환한다
- [x] 접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [x] 채팅방 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [x] 채팅방 이름 속성이 중복되면 `400 Bad Request` 상태코드를 반환한다
- [x] 채팅방 이름을 반환한다
- [ ] 채팅방 생성일시를 반환한다

### 채팅방 퇴장

요청

- 메서드: `POST`
- URL: `/api/rooms/{roomId}/leave`
- 헤더: `Content-Type: application/json`
- 경로 변수: `roomId` (채팅방 ID)
- 본문

```json
{
  "name": "사용자 이름"
}
```

- 성공 응답

```json
{
  "message": "채팅방에서 퇴장했습니다."
}
```

### 채팅 메시지 전송

요청

- 메서드: `POST`
- URL: `/chat/rooms/{roomId}/messages`
- 헤더: `Content-Type: application/json`
- 경로 변수: `roomId` (채팅방 ID)
- 본문

```
ChatMessageSendCommand {
  "username": "사용자 이름",
  "message": "보낼 메시지"
}
```

- 성공 응답

```json
{
  "id": "메시지 ID",
  "roomId": "채팅방 ID",
  "sender": "사용자 이름",
  "message": "보낼 메시지",
  "createdAt": "생성일시"
}
```

- curl 명령 예시

```bash
curl -i -X POST 'http://localhost:8080/chat/rooms/{roomId}/messages' \
-H 'Content-Type: application/json' \
-d '{
  "name": "사용자 이름",
  "message": "보낼 메시지"
}'
```

테스트

- [ ] 올바르게 요청하면 `200 ok` 상태코드를 반환한다
- [ ] 접근 토큰을 사용하지 않으면 `401 Unauthorized` 상태코드를 반환한다
- [ ] 채팅방 ID가 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [ ] 사용자 이름 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다
- [ ] 메시지 속성이 지정되지 않으면 `400 Bad Request` 상태코드를 반환한다

### 채팅 메시지 조회

요청

- 메서드: `GET`
- URL: `/api/rooms/{roomId}/messages`
- 헤더: `Content-Type: application/json`
- 경로 변수: `roomId` (채팅방 ID)
- 본문 없음

```json
{
  "page": 0,
  "size": 10
}
```

### 채팅방 삭제

요청

- 메서드: `DELETE`
- URL: `/api/rooms/{roomId}`
- 헤더: `Content-Type: application/json`
- 경로 변수: `roomId` (채팅방 ID)
- 본문 없음
- 성공 응답

```json
{
  "message": "채팅방이 삭제되었습니다."
}
```

### 채팅방 정보 조회

요청

- 메서드: `GET`
- URL: `/api/rooms/{roomId}`
- 헤더: `Content-Type: application/json`
- 경로 변수: `roomId` (채팅방 ID)
- 본문 없음
- 성공 응답

```json
{
  "id": "채팅방 ID",
  "name": "채팅방 이름",
  "createdAt": "생성일시",
  "users": [
    {
      "id": "사용자 ID",
      "name": "사용자 이름"
    },
    ...
  ]
}
```

## 테스트

```bash
./gradlew test
```

## 코드 스타일 검사

```bash
./gradlew check
```

## 코드 스타일 자동 수정

```bash
./gradlew spotlessApply
```

## 라이센스

이 프로젝트는 [MIT 라이센스](https://opensource.org/license/mit/)를 따릅니다.

