package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public boolean existsByUsername(String username) {
        return are.existsByUsername(username);
    }

    public List<User> findByCnt(Integer cnt) {
        return are.findAll(PageRequest.of(cnt, 10, Sort.by("id").ascending())).getContent();
    }
}
