package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.domain.entity.User;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.security.UserPrincipal;
import com.doan.cinemaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME,new String[]{username}));
        return UserPrincipal.create(user);
    }
}
