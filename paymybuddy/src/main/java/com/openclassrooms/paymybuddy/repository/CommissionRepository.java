package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    Commission findById(int id);

}