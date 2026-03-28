package com.ttcsn5.webstudyenglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttcsn5.webstudyenglish.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {

    Role findByCode(String code);

}
