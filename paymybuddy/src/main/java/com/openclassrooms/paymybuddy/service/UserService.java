package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User addUser (User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User findById (int id) {
        User user = userRepository.findById(id);
        if(user==null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User findByUsername (String username) {
        User user = userRepository.findByUsername(username);
        if(user==null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User addConnectionToUser (User connection, User user) {
        if (connection != null) {
            if (user.addConnection(connection)) {
                userRepository.save(user);
                return user;
            }
        }
        return null;
    }
}
