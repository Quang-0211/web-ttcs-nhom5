package com.ttcsn5.webstudyenglish.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ttcsn5.webstudyenglish.entity.Role;
import com.ttcsn5.webstudyenglish.repository.RoleRepo;

@Service
public class RoleService {
    @Autowired
    private RoleRepo rre;

    public void saveRole(Role role) {
        rre.save(role);
    }
    public Role findByCode(String code) {
        return rre.findByCode(code);
    }
}
