package com.joinproject.global.login;


import com.joinproject.domain.member.Member;
import com.joinproject.domain.member.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinproject.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    ObjectMapper objectMapper = new ObjectMapper();

    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";
    private static String USERNAME = "username";
    private static String PASSWORD = "123456789";

    private static String LOGIN_RUL = "/login";


    private void clear(){
        em.flush();
        em.clear();
    }

    @BeforeEach
    private void init(){
        memberRepository.save(Member.builder()
                .username(USERNAME)
                .password(delegatingPasswordEncoder.encode(PASSWORD))
                .name("Member1")
                .nickname("NickName1")
                .role(Role.USER)
                .age(22)
                .build());
        clear();
    }

    private Map getUsernamePasswordMap(String username, String password){
        Map<String, String> map = new HashMap<>();
        map.put(KEY_USERNAME, username);
        map.put(KEY_PASSWORD, password);
        return map;
    }

    private ResultActions perform(String url, MediaType mediaType, Map usernamePasswordMap) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .contentType(mediaType)
                .content(objectMapper.writeValueAsString(usernamePasswordMap)));

    }

    // 로그인 성공
    @Test
    public void 로그인_성공() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(USERNAME, PASSWORD);


        //when, then
        MvcResult result = perform(LOGIN_RUL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    // 로그인 실패 - 아이디 오류
    @Test
    public void 로그인_실패_아이디틀림() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(USERNAME+"123", PASSWORD);

        //when, then
        MvcResult result = perform(LOGIN_RUL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // 로그인 실패 - 비밀번호 오류
    @Test
    public void 로그인_실패_비밀번호틀림() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(USERNAME, PASSWORD+"123");


        //when, then
        MvcResult result = perform(LOGIN_RUL, APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    // 로그인 주소가 틀리면 Forbidden
    @Test
    public void 로그인_주소가_틀리면_FORBIDDEN() throws Exception {
        //given
        Map<String, String> map = getUsernamePasswordMap(USERNAME, PASSWORD);


        //when, then
        perform(LOGIN_RUL+"123", APPLICATION_JSON, map)
                .andDo(print())
                .andExpect(status().isForbidden());

    }

}
