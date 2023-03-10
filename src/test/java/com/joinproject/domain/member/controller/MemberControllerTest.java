package com.joinproject.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinproject.domain.member.Member;
import com.joinproject.domain.member.dto.MemberSignUpDto;
import com.joinproject.domain.member.exception.MemberExceptionType;
import com.joinproject.domain.member.repository.MemberRepository;
import com.joinproject.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    PasswordEncoder passwordEncoder;

    private static String SIGN_UP_URL = "/signUp";

    private String username = "username";
    private String password = "password1234@";
    private String name = "shinD";
    private String nickname = "shinD cute";
    private Integer age = 22;

    private void clear(){
        em.flush();
        em.clear();
    }

    private void signUp(String signUpData) throws Exception {
        mockMvc.perform(
                        post(SIGN_UP_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signUpData))
                .andExpect(status().isOk());
    }

    private void signUpFail(String signUpData) throws Exception {
        mockMvc.perform(
                        post(SIGN_UP_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(signUpData))
                .andExpect(status().isBadRequest());
    }

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String BEARER = "Bearer ";

    private String getAccessToken() throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);


        MvcResult result = mockMvc.perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getHeader(accessHeader);
    }

    @Test
    public void ????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));

        //when
        signUp(signUpData);

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new Exception("????????? ????????????")
        );

        assertThat(member.getName()).isEqualTo(name);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void ????????????_??????_?????????_??????() throws Exception {
        //given
        String noUsernameSignUpData = objectMapper.writeValueAsString(new MemberSignUpDto(null, password, name, nickname, age));
        String noPasswordSignUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, null, name, nickname, age));
        String noNameSignUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, null, nickname, age));
        String noNickNameSignUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, null, age));
        String noAgeSignUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, null));

        //when, then
        /*signUp(noUsernameSignUpData);//????????? ?????????????????? ??????????????? 200
        signUp(noPasswordSignUpData);//????????? ?????????????????? ??????????????? 200
        signUp(noNameSignUpData);//????????? ?????????????????? ??????????????? 200
        signUp(noNickNameSignUpData);//????????? ?????????????????? ??????????????? 200
        signUp(noAgeSignUpData);//????????? ?????????????????? ??????????????? 200*/

        signUpFail(noUsernameSignUpData);//????????? ???????????? ??????????????? 400
        signUpFail(noPasswordSignUpData);//????????? ???????????? ??????????????? 400
        signUpFail(noNameSignUpData);//????????? ???????????? ??????????????? 400
        signUpFail(noNickNameSignUpData);//????????? ???????????? ??????????????? 400
        signUpFail(noAgeSignUpData);//????????? ???????????? ??????????????? 400

        assertThat(memberRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void ??????????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));

        signUp(signUpData);

        String accessToken = getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("name",name+"??????");
        map.put("nickname",nickname+"??????");
        map.put("age",age+1);
        String updateMemberData = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(
                        put("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateMemberData))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member.getName()).isEqualTo(name+"??????");
        assertThat(member.getNickname()).isEqualTo(nickname+"??????");
        assertThat(member.getAge()).isEqualTo(age+1);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    public void ??????????????????_????????????????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();
        Map<String, Object> map = new HashMap<>();
        map.put("name",name+"??????");
        String updateMemberData = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(
                        put("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateMemberData))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member.getName()).isEqualTo(name+"??????");
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getAge()).isEqualTo(age);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void ??????????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);
        map.put("toBePassword",password+"!@#@!#@!#");

        String updatePassword = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(
                        put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(password+"!@#@!#@!#", member.getPassword())).isTrue();
    }

    @Test
    public void ??????????????????_??????_?????????????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password+"1");
        map.put("toBePassword",password+"!@#@!#@!#");

        String updatePassword = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(
                        put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(password+"!@#@!#@!#", member.getPassword())).isFalse();
    }

    @Test
    public void ??????????????????_??????_????????????_????????????_??????_??????????????????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);
        map.put("toBePassword","123123");

        String updatePassword = objectMapper.writeValueAsString(map);


        //when
        mockMvc.perform(
                        put("/member/password")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
//                .andExpect(status().isOk())
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("123123", member.getPassword())).isFalse();
    }

    @Test
    public void ????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        assertThrows(Exception.class, () -> memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????")));
    }

    @Test
    public void ????????????_??????_??????????????????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password+11);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member).isNotNull();

    }

    @Test
    public void ????????????_??????_???????????????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword",password);

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/member")
                                .header(accessHeader,BEARER+accessToken+"1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isForbidden());

        //then
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member).isNotNull();
    }

    @Test
    public void ???????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when
        MvcResult result = mockMvc.perform(
                        get("/member")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isOk()).andReturn();


        //then
        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member.getAge()).isEqualTo(map.get("age"));
        assertThat(member.getUsername()).isEqualTo(map.get("username"));
        assertThat(member.getName()).isEqualTo(map.get("name"));
        assertThat(member.getNickname()).isEqualTo(map.get("nickname"));

    }

    @Test
    public void ???????????????_??????_JWT??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when,then
        mockMvc.perform(
                        get("/member")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken+1))
                .andExpect(status().isForbidden());

    }


    /**
     * ?????????????????? ??????
     * ?????????????????? ?????? -> ???????????????
     * ?????????????????? ?????? -> ???????????????
     */
    @Test
    public void ??????????????????_??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();

        Long id = memberRepository.findAll().get(0).getId();

        //when

        MvcResult result = mockMvc.perform(
                        get("/member/"+id)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isOk()).andReturn();


        //then
        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new Exception("????????? ????????????"));
        assertThat(member.getAge()).isEqualTo(map.get("age"));
        assertThat(member.getUsername()).isEqualTo(map.get("username"));
        assertThat(member.getName()).isEqualTo(map.get("name"));
        assertThat(member.getNickname()).isEqualTo(map.get("nickname"));
    }

    @Test
    public void ??????????????????_??????_??????????????????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when
        MvcResult result = mockMvc.perform(
                        get("/member/2211")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken))
                //.andExpect(status().isOk()).andReturn();
                .andExpect(status().isNotFound()).andReturn();

        //then
        Map<String, Integer> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        assertThat(map.get("errorCode")).isEqualTo(MemberExceptionType.NOT_FOUND_MEMBER.getErrorCode()); //??? ?????????
    }

    @Test
    public void ??????????????????_??????_JWT??????() throws Exception {
        //given
        String signUpData = objectMapper.writeValueAsString(new MemberSignUpDto(username, password, name, nickname, age));
        signUp(signUpData);

        String accessToken = getAccessToken();


        //when,then
        mockMvc.perform(
                        get("/member/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessToken+1))
                .andExpect(status().isForbidden());

    }

}