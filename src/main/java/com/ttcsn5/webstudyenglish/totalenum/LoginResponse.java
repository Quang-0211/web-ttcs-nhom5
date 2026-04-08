package com.ttcsn5.webstudyenglish.totalenum;

import com.ttcsn5.webstudyenglish.entity.User;

public class LoginResponse {
    private LoginStatus status;
    private User user;

    public LoginResponse(LoginStatus status, User user) {
        this.status = status;
        this.user = user;
    }

    public LoginStatus getStatus() {
        return status;
    }

    public void setStatus(LoginStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
