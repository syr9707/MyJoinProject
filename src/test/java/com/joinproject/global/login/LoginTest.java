package com.joinproject.global.login;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 성공 - 200에 성공 메세지 반환
 * 로그인 실패 - 아이디 틀림 - 200에 실패 메세지
 * 로그인 실패 - 비밀번호 틀림 -200에 실패 메세지
 * 로그인 주소가 틀리면 403 Forbidden
 * 로그인 데이터형식이 Json이 아니면 200에 실패 메세지 (로그인 실패와 동일)
 * 로그인 Http Method가 Post가 아니면 404 NotFound
 * */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    

}
