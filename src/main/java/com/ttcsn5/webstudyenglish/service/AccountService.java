package com.ttcsn5.webstudyenglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.User;
import com.ttcsn5.webstudyenglish.repository.AccountRepo;
import com.ttcsn5.webstudyenglish.totalenum.LoginResponse;
import com.ttcsn5.webstudyenglish.totalenum.LoginStatus;
import com.ttcsn5.webstudyenglish.totalenum.RegisterStatus;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AccountService {
    @Autowired
    private AccountRepo are;
    @Autowired
    private HashPassword hashPassword;

    public List<User> findByCnt(Integer cnt) {
        return are.findAll(PageRequest.of(cnt, 10, Sort.by("id").ascending())).getContent();
    }

    public LoginResponse checkLogin(String email, String password) {
        User user = this.findByEmail(email);
        if (user == null) {
            return new LoginResponse(LoginStatus.INVALID_EMAIL, null);
        } else if (hashPassword.checkPassword(password, user.getPassword()) == false) {
            return new LoginResponse(LoginStatus.INVALID_PASSWORD, null);
        }
        return new LoginResponse(LoginStatus.SUCCESS, user);
    }

    public RegisterStatus checkRegister(String username, String email, String password) {
        if (this.existsByEmail(email)) {
            return RegisterStatus.EMAIL_EXISTS;
        }
        if (this.existsByUsername(username)) {
            return RegisterStatus.USERNAME_EXISTS;
        }
        return RegisterStatus.SUCCESS;
    }

    public List<User> searchUser(String username, String email, int roleId, int cnt) {
        Pageable pageable = PageRequest.of(cnt, 10, Sort.by("id").ascending());

        if (roleId == 0) {
            return are.findByUsernameContainingAndEmailContaining(username, email, pageable)
                    .getContent();
        }

        return are.findByUsernameContainingAndEmailContainingAndRoleId_Id(
                username, email, roleId, pageable).getContent();
    }

    public List<User> searchByUsernameEmailRole(String username, String email, int role) {
        return are.searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndRoleId_Id(username,
                email, role);
    }

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

    public List<User> searchByUsernameEmail(String username, String email) {
        return are.searchByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username,
                email);
    }
}
