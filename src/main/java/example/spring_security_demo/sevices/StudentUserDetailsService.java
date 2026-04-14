package com.example.spring_security_demo.sevices;

import com.example.spring_security_demo.models.StudentUserDetails;
import com.example.spring_security_demo.models.User;
import com.example.spring_security_demo.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u =  userRepo.findByUsername(username);
        if(u == null){
            throw new UsernameNotFoundException("User not found");
        }
        return new StudentUserDetails(u);
    }
}
