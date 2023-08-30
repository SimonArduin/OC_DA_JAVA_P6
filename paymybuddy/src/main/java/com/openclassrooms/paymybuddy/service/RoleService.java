package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Role;
import com.openclassrooms.paymybuddy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    public Role findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return roleRepository.findById(id);
    }
}