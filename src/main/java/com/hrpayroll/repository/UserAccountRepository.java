package com.hrpayroll.repository;

import com.hrpayroll.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JPA repository for UserAccount entity
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    // Custom query method to find user by username
    Optional<UserAccount> findByUsername(String username);
}
