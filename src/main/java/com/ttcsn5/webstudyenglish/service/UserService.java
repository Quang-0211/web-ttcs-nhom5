package com.ttcsn5.webstudyenglish.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;

@Service
public class UserService {

    @Autowired
    private AccountRepo accountRepo;

    public Optional<User> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return accountRepo.findById(id.longValue());
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return accountRepo.findById(id);
    }
}
