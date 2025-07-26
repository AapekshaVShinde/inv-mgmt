package com.inventory.security;

import com.inventory.entity.User;
import com.inventory.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserRepository repository;

    public UserInfoService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = repository.findByEmailAndActiveTrue(emailOrUsername)
                .orElseGet(() -> repository.findByUsernameAndActiveTrue(emailOrUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found " + emailOrUsername)));

        return new UserInfoDetails(user);
    }
}

