package com.ttcsn5.webstudyenglish.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ttcsn5.webstudyenglish.entity.Role;
import com.ttcsn5.webstudyenglish.repository.RoleRepo;

@Component
public class DataIn implements CommandLineRunner {
    @Autowired
    private RoleRepo rre;

    @Override
    public void run(String... args) throws Exception {
        if (rre.findByCode("ADMIN") == null) {
            rre.save(new Role("ADMIN", "Admin"));
        }
        if (rre.findByCode("USER") == null) {
            rre.save(new Role("USER", "User"));
        }
    }

}
