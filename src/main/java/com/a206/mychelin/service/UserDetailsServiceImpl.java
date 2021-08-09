package com.a206.mychelin.service;

import com.a206.mychelin.config.MyUserDetails;
import com.a206.mychelin.domain.entity.User;
import com.a206.mychelin.domain.repository.UserRepository;
import com.a206.mychelin.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public MyUserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            if (!user.get().isWithdraw()) {
                return new MyUserDetails(user.get(), Collections.singleton(new SimpleGrantedAuthority(user.get().getRole())));
            }
            throw new UsernameNotFoundException(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}