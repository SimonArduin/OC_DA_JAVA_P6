package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.Commission;
import com.openclassrooms.paymybuddy.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommissionService {
    @Autowired
    CommissionRepository commissionRepository;
    public Commission findById(Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return commissionRepository.findById(id);
    }
}
