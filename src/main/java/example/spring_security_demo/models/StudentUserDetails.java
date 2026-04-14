package com.example.spring_security_demo.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class StudentUserDetails implements UserDetails {

    User user;

    public StudentUserDetails(User user) {
    this.user = user;
    }

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        if (user.getRoles() == null || user.getRoles().isBlank()) {
//            return List.of();
//        }
//        return Arrays.stream(user.getRoles().split(","))
//                .map(String::trim)
//                .filter(r -> !r.isEmpty())
//                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
//                .map(r -> (GrantedAuthority) () -> r)
//                .toList();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
