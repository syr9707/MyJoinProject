package com.joinproject.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joinproject.domain.member.repository.MemberRepository;
import com.joinproject.domain.member.service.LoginService;
import com.joinproject.global.login.filter.JsonUsernamePasswordAuthenticationFilter;
import com.joinproject.global.login.handler.LoginFailureHandler;
import com.joinproject.global.login.handler.LoginSuccessJWTProvideHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoginService loginService;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
//    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable() //1 - formLogin 인증방법 비활성화
                .httpBasic().disable() //2 - httpBasic 인증방법 비활성화(특정 리소스에 접근할 때 username과 password 물어봄)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/login", "/signUp","/").permitAll()
                .anyRequest().authenticated();

        http.addFilterAfter(jsonUsernamePasswordLoginFilter(), LogoutFilter.class);

        return http.build();
    }

    /**
     * passwordEncoderForEncode로는 BCryptPasswordEncoder를 사용하는 DelegatingPasswordEncoder를 사용한다.
     *
     * DB에 데이터를 저장할 때, Bcrypt로 저장을 한다면 이는 암호화되어서 저장되고, 이를 복호화 할 수 있는 방법은 없다.
     * 즉, 비밀번호의 비교는 가능하나 복호화는 불가능 하다.
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){ //1 - PasswordEncoder 등록
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {//2 - AuthenticationManager 등록
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();//DaoAuthenticationProvider 사용
        provider.setPasswordEncoder(passwordEncoder());//PasswordEncoder로는 PasswordEncoderFactories.createDelegatingPasswordEncoder() 사용
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler(){
        return new LoginSuccessJWTProvideHandler();
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter(){
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        //jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        //jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }
}
