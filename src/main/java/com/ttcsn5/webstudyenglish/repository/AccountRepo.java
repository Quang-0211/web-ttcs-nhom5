package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.User;

@Repository
public interface AccountRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByUsername(String username);

}
