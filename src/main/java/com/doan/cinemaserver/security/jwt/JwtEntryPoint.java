package com.doan.cinemaserver.security.jwt;

import com.doan.cinemaserver.common.RestData;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtEntryPoint implements AuthenticationEntryPoint {
    private final MessageSource messageSource;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message=messageSource.getMessage(ErrorMessage.UNAUTHORIZED,null, LocaleContextHolder.getLocale());
        response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(RestData.error(HttpStatus.UNAUTHORIZED.value(), message)));
    }
}
