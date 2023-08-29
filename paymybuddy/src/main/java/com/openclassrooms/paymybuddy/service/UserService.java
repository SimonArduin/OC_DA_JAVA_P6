package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserDto addUser (UserDto userDto) {
        if(userDto == null || userDto.isEmpty())
            throw new IllegalArgumentException();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        return new UserDto(userRepository.save(new User(userDto)));
    }

    public UserDto findById (Integer id) {
        if(id==null)
            throw new IllegalArgumentException();
        User user = userRepository.findById(id);
        if(user!=null && !user.isEmpty())
            return new UserDto(user);
        return null;
    }

    public UserDto findByUsername (String username) {
        if(username==null)
            throw new IllegalArgumentException();
        User user = userRepository.findByUsername(username);
        if(user!=null && !user.isEmpty())
            return new UserDto(user);
        return null;
    }

    public UserDto addConnectionToUser (UserDto userDto, UserDto connection) {
        if (userDto == null)
            throw new IllegalArgumentException("Invalid user");
        if (connection == null || connection.isEmpty())
            throw new IllegalArgumentException("Invalid connection");
        if (userDto.addConnection(connection)) {
            User user = userRepository.save(new User(userDto));
            return new UserDto(user);
        }
        return null;
    }

    public UserDto removeFromAccountBalance(UserDto userDto, Double amount) {
        if(userDto==null || userDto.isEmpty() || !userDto.equals(new UserDto(userRepository.findById(userDto.getId()))))
            throw new IllegalArgumentException("Invalid user");
        if(amount == null || amount <= 0)
            throw new IllegalArgumentException("No amount to remove");
        if(userDto.getAccountBalance()-amount>=0) {
            userDto.setAccountBalance(userDto.getAccountBalance() - amount);
            User user = userRepository.save(new User(userDto));
            return new UserDto(user);
        }
        return null;
    }

    public UserDto addToAccountBalance(UserDto userDto, Double amount) {
        if(userDto==null || userDto.isEmpty() || !userDto.equals(new UserDto(userRepository.findById(userDto.getId()))))
            throw new IllegalArgumentException("Invalid user");
        if(amount == null || amount <= 0)
            throw new IllegalArgumentException("No amount to add");
        userDto.setAccountBalance(userDto.getAccountBalance()+amount);
        return new UserDto(userRepository.save(new User(userDto)));
    }
}
