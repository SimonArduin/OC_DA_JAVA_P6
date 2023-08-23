package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User addUser (User user) {
        if(user == null || user.isEmpty())
            throw new IllegalArgumentException();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User findById (Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        return userRepository.findById(id);
    }

    public User findByUsername (String username) {
        if(username==null)
            throw new IllegalArgumentException();
        return userRepository.findByUsername(username);
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
