package com.joinproject.global.jwt.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JwtService {

    String createAccessToken(String username);
    String createRefreshToken();

    void updateRefreshToken(String username, String refreshToken);

    void destroyRefreshToken(String username);

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);
    void sendAccessToken(HttpServletResponse response, String accessToken);

    String extractAccessToken(HttpServletRequest request) throws IOException, ServletException;

    String extractRefreshToken(HttpServletRequest request) throws IOException, ServletException;

    String extractUsername(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);
    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

}
