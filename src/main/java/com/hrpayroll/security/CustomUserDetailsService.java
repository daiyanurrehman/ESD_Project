package com.hrpayroll.security;

import com.hrpayroll.entity.UserAccount;
import com.hrpayroll.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Custom UserDetailsService to load user from database
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final UserAccountRepository userAccountRepository;

    public CustomUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new UsernameNotFoundException("Username cannot be null or empty");
            }
            
            UserAccount userAccount;
            try {
                userAccount = userAccountRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            } catch (DataAccessException e) {
                logger.error("Database error while loading user: {}", username, e);
                throw new UsernameNotFoundException("Database error while loading user: " + username, e);
            }

            if (userAccount.getRole() == null) {
                logger.warn("User {} has no role assigned", username);
                throw new UsernameNotFoundException("User has no role assigned: " + username);
            }

            return new User(
                    userAccount.getUsername(),
                    userAccount.getPasswordHash(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userAccount.getRole().name())));
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error loading user: {}", username, e);
            throw new UsernameNotFoundException("Unexpected error loading user: " + username, e);
        }
    }
}
