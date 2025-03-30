package com.doan.cinemaserver.service;

import org.springframework.stereotype.Service;

@Service
public interface JwtTokenService {
    public void saveInvalidToken(String token);
    public boolean isInvalidToken(String token);
}
