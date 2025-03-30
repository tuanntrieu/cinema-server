package com.doan.cinemaserver.service.impl;


import com.doan.cinemaserver.security.jwt.JwtTokenProvider;
import com.doan.cinemaserver.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void saveInvalidToken(String token) {
        redisTemplate.opsForValue().set(token,jwtTokenProvider.getTokenTypeFromJwt(token),jwtTokenProvider.getRemainingTime(token), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isInvalidToken(String token) {
        return redisTemplate.hasKey(token);
    }
}
