## Access / Refresh Token 발행과 검증 테스트 시나리오

### Access / Refresh Token 발행 테스트 시나리오
- 로그인 성공 시 Access / Refresh Token 발급.
- RefreshToken 테이블에 이미 RefreshToken이 있다면 새로 발급한 RefreshToken으로 갱신
- RefreshToken 테이블에 RefreshToken이 없다면 새로 추가

### Access / Refresh Token 검증 테스트 시나리오

- api 요청 시 Access 토큰 검증 -> 만료 응답 -> 클라이언트  Refresh Token 을 포함하여 토큰 재발급 요청
                 -> Refresh Token 검증 후 현재 시간 기점으로 Access / Refresh Token 재발급

## 회원가입
`/auth/signup`

```json
{
  "username": "JIN HO",
  "password": "12341234",
  "nickname": "Mentos"
}
```

## 로그인
`/auth/sign`

```json
{
  "username": "JIN HO",
  "password": "12341234"
}
```

## Refresh 토큰 이용해 Access / Refresh 토큰 재발급
`/auth/refresh`

```json
{
  "accessToken":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwidXNlcm5hbWUiOiJKSU4g44WHSE8iLCJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlhdCI6MTczNzE4NTUwMywiZXhwIjoxNzM3MTg3MzAzfQ.MwrYzhPdHWLEsIaphzQboH7lJqfys8e-8u8HxpJT7edSnMv9fFF5_N_pRsau2GXclOLKODLSChgo0KzSneklBg",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MzcxODU1MDMsImV4cCI6MTczNzc5MDMwM30.8CVP2JRmxP3O3-fZXYkOy5NItAt2uSG2z7cO6rurG-3Qf5W_sRdeVBqk06gAu_sxE0ECLqBcMuqYoGVE-bfBnA"
}
```
