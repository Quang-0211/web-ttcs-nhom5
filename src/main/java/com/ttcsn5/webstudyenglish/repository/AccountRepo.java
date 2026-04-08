package com.ttcsn5.webstudyenglish.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.User;

@Repository
public interface AccountRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    List<User> searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndRoleId_Id(String username,
            String email, int role);

    List<User> searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email);


    Slice<User> findByUsernameContainingAndEmailContaining(String username, String email, Pageable pageable);

    Slice<User> findByUsernameContainingAndEmailContainingAndRoleId_Id(String username, String email, int roleId, Pageable pageable);
}
