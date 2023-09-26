package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Role;
import com.openclassrooms.paymybuddy.exception.RoleNotFoundException;
import com.openclassrooms.paymybuddy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Role findById(Integer id) {

        if(id == null)
            throw new IllegalArgumentException("Invalid id");

        Role result = roleRepository.findById(id);

        if(result == null)
            throw new RoleNotFoundException("Role not found");

        return result;

    }
}