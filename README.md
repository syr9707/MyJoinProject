# MyJoinProject
> Spring 게시판 API 만들기

<br>

# 🛠️ 사용 기술
* Java
* Spring Boot
* Spring Security
* Spring Data JPA

<br>

# 📌 프로젝트 목표
* 회원가입 흐름 완벽히 이해하기
* Spring-Security 흐름 이해하기
* jwt 이해하기
* Custom Exception 이해하기
* Test Code 다양하게 접해보기

<br>

## ✔ 조건
* view는 구현하지 않고, API만 제공한다.

<br>

## ✔ 요구사항
1. 로그인 기능
   * Json Wev Token을 이용한 토큰 인증 방식
     * 하나의 애플리케이션 내에서 사용자 인증과 토큰 발급을 모두 수행한다.
   * 아이디, 비밀번호, 이름, 닉네임, 나이를 입력받는다.
   * 아이디는 중복될 수 없다.
   * 비밀번호, 이름, 닉네임, 나이는 변경할 수 있다.
   * 비밀번호는 암호화되어 데이터베이스에 저장한다.
   * 로그인을 성공하면 JWT를 발급해준다.
2. 게시글 CRUD
   * 회원과 연관관계를 맺는다.
   * 댓글과 연관관계를 맺는다.
   * 권한이 없으면, 게시글을 등록하지 못한다.
3. 게시글 댓글 CRUD
   * 회원과 연관관계를 맺는다.
   * 게시글과 연관관계를 맺는다.
   * 대댓글이 남아있을 때, 그 댓글을 삭제하는 경우, DB와 화면에서는 지워지지 않는다.
   * 권한이 없으면, 댓글을 등록하거나 수정할 수 없다.
4. 게시글에 파일 업로드를 할 수 있다.
5. 각 도메인에 관한 예외는 Custom Exception 한다.
6. 각 권한 테스트는 MockTest로 한다.

<br>

## ✔ API
1. Member (회원)
   * 회원 가입 : POST - /signUp
   * 정보 수정 : PUT - /member
   * 비밀번호 수정 : PUT - /member/password
   * 회원 탈퇴 : DELETE - /member
   * 회원 조회 : GET - /member/{id}
   * 내정보 조회 : GET - /member
2. Post (게시글)
   * 게시글 등록 : POST - /post
   * 게시글 수정 : PUT - /post/{id}
   * 게시글 삭제 : DELETE - /post/{id}
   * 게시글 조회 : GET - /post/{id}
   * 게시글 검색 : GET - /post
3. Comment (댓글)
   * 댓글 등록 : POST - /comment/{postId}
   * 대댓글 등록 : POST - /comment/{postId}/{commentId}
   * 댓글 수정 : PUT - /comment/{commentId}
   * 댓글 삭제 : DELETE - /comment/{commentId}

<br>

## ❓ Why?
1. JWT를 왜 사용했나?
   * JWT가 Session에 비해 가지는 장점
     * 가장 큰 차이는 서버에 인증 정보를 저장하지 않는다.
     * 그렇기 때문에, 클라이언트의 요청마다 인증을 위해 DB를 탐색하는 과정이 필요하지 않고, (RefreshToken을 사용하는 경우는 줄어듦)
     * 저장 공간도 필요하지 않다.
2. JWT는 언제 사용할까?
   1. 권한 부여
      * 사용자가 로그인하면 각 요청이 JWT가 포함되어, 사용자가 해당 토큰으로 서비스 및 리소스에 접근할 수 있다.
   2. 정보 교환
      * 공개/개인키 쌍을 사용하여 JWT에 서명할 수 있기 떄문에 발신자 확인이 가능하다.
      * 또한 헤더와 페이로드를 사용하여 서명을 하므로, 변조 여부를 파악할 수 있다.
3. JWT에 관하여
   * JWT는 DB에 저장하는 방식이 아니기 때문에 DB 리소스가 필요 없다. (RefreshToken을 사용하는 경우 제외)
     * RefreshToken은 DB에 저장되며, 긴 만료시간을 가지고 있다.
     * 사용자는 JWT 토큰이 만료되면, 리프레시 토큰을 이용해 갱신을 하여 사용하게 된다.
     * 💡 그래도 Session 방식처럼 매번 DB에 접근하는 것보다는 훨씬 적은 횟수의 접근이다.
   * JWT 토큰을 사용하면, session처럼 특정 session DB에 접근하는 게 아니다.
     * JWT 토큰 검증 로직을 통해 여러 서비스 간 통신에서 권한을 쉽게 제한하고 허가할 수 있다.
   * 🚨 토큰에 담고있는 정보가 많아질수록 데이터의 크기가 커지기 때문에, 네트워크 부하가 생길 수 있다.

<br>

## 🤔 JWT의 장단점
💡 장점
* JWT를 이용하면 따로 서버의 메모리에 저장 공간을 확보할 필요가 없다.
* 서버가 토큰을 한번 클라이언트에게 보내주면, 클라이언트는 토큰을 보관하고 있다가 요청을 보낼 때마다 헤더에 토큰을 실어보내면 된다.
* 쿠키를 사용할 수 없는 모바일 어플리케이션에는 JWT를 사용한 인증방식이 최적이다.

🚨 단점
* JWT는 HTTP를 통해서 전송하기 때문에, 페이로드의 크기가 클수록 데이터 전송에 있어서 비용이 커진다.
* JWT는 유효기간을 따로 정하지 않는 이상 소멸되지 않기 때문에 장기간 방치시 해킹의 위험이 커진다.

<br>

# ▶️DB 구조
![](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/8cc77a17-6baa-4a50-9c6a-832c547cf02a/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230310%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230310T051901Z&X-Amz-Expires=86400&X-Amz-Signature=467d2acf8efb66497f86128fbc457c534483b6f7a9b930a34ab179f9b9127b45&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled.png%22&x-id=GetObject)


