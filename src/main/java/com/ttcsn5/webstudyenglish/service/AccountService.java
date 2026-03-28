package com.ttcsn5.webstudyenglish.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepo are;

    public AccountRepo getAre() {
        return are;
    }

    public void setAre(AccountRepo are) {
        this.are = are;
    }

    public boolean existsByEmail(String email) {
        return are.existsByEmail(email);
    }

    public void saveUser(User user) {
        are.save(user);
    }

    public User findByEmail(String email) {
        return are.findByEmail(email);
    }
}
