package com.isssr.ticketing_system.service;

import com.isssr.ticketing_system.exception.EmailNotFoundException;
import com.isssr.ticketing_system.model.Privilege;
import com.isssr.ticketing_system.model.Role;
import com.isssr.ticketing_system.model.User;
import com.isssr.ticketing_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws EmailNotFoundException {
        if (!userRepository.existsByEmail(s))
            throw new EmailNotFoundException(String.format("The email %s doesn't exist", s));

        User user = userRepository.findByEmail(s).get();

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles)
            for (Privilege privilege : role.getPrivileges())
                authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        return authorities;
    }
}
