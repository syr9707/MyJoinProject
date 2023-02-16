# MyJoinProject
> Spring 게시판 API 만들기

# 🛠️ 사용 기술
* Java
* Spring Boot
* Spring Security
* Spring Data JPA

# 📌 프로젝트 목표
* 회원가입 흐름 완벽히 이해하기
* Spring-Security 흐름 이해하기
* jwt 이해하기

## ✔ 조건
* view는 구현하지 않고, API만 제공한다.

## ✔ 요구사항
* 로그인 기능
  * Json Wev Token을 이용한 토큰 인증 방식
    * 하나의 애플리케이션 내에서 사용자 인증과 토큰 발급을 모두 수행한다.
  * 아이디, 비밀번호, 이름, 닉네임, 나이를 입력받는다.
  * 아이디는 중복될 수 없다.
  * 비밀번호, 이름, 닉네임, 나이는 변경할 수 있다.
  * 비밀번호는 암호화되어 데이터베이스에 저장한다.
  * 로그인을 성공하면 JWT를 발급해준다.
* 게시글 CRUD
  * 회원과 연관관계를 맺는다.
  * 댓글과 연관관계를 맺는다.
* 게시글 댓글 CRUD
  * 회원과 연관관계를 맺는다.
  * 게시글과 연관관계를 맺는다.

# DB 구조
![](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/84a15397-bc2d-4924-b4a4-635f7fe819d2/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230216%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230216T075823Z&X-Amz-Expires=86400&X-Amz-Signature=393792e520755440b274386a946690cf395e2b6ec92c0eb78528d48c13c23fdd&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)

