package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(int id);

}