package com.uit.realestate.service;

import com.uit.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    /**
     * password: password
     * password encrypt
     */
    final String defaultPassword = "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6";
    final String defaultPass123 = "$2a$10$.JvCcIOq4LLAzmiW4ZF2oOQd5uC9uTAHL3h2CWUSiYOCX7/Nuz5Ee";

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.uit.realestate.domain.user.User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

