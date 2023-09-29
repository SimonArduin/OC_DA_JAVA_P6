package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.UserDto;
import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.exception.UserNotFoundException;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, timeout = 60)
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserDto addUser (UserDto userDto) {

        if(userDto == null || userDto.isEmpty())
            throw new IllegalArgumentException("Invalid user");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        return new UserDto(userRepository.save(new User(userDto)));

    }

    public UserDto findById (Integer id) {

        if(id==null)
            throw new IllegalArgumentException("Invalid id");

        User user = userRepository.findById(id);

        if(user==null || user.isEmpty())
            throw new UserNotFoundException("User not found");

        return new UserDto(user);

    }

    public UserDto findByUsername (String username) {

        if(username==null)
            throw new IllegalArgumentException("Invalid username");

        User user = userRepository.findByUsername(username);

        if(user==null || user.isEmpty())
            throw new UserNotFoundException("User not found");

        return new UserDto(user);

    }

    public UserDto addConnectionToUser (UserDto userDto, UserDto connection) {

        if (userDto == null)
            throw new IllegalArgumentException("Invalid user");

        if (connection == null || connection.isEmpty())
            throw new IllegalArgumentException("Invalid connection");

        if (!userDto.addConnection(connection))
            throw new IllegalArgumentException("Could not add connection to user");

        else {

            User user = userRepository.save(new User(userDto));

            return new UserDto(user);

        }

    }

    public UserDto removeFromAccountBalance(UserDto userDto, Double amount) {

        if(userDto==null || userDto.isEmpty() || !userDto.equals(new UserDto(userRepository.findById(userDto.getId()))))
            throw new IllegalArgumentException("Invalid user");

        if(amount == null || amount <= 0)
            throw new IllegalArgumentException("Invalid amount");

        if(userDto.getAccountBalance() < amount)
            throw new IllegalArgumentException("Not enough money on user account");

        userDto.setAccountBalance(userDto.getAccountBalance() - amount);

        User user = userRepository.save(new User(userDto));

        return new UserDto(user);

    }

    public UserDto addToAccountBalance(UserDto userDto, Double amount) {

        if(userDto==null || userDto.isEmpty() || !userDto.equals(new UserDto(userRepository.findById(userDto.getId()))))
            throw new IllegalArgumentException("Invalid user");

        if(amount == null || amount <= 0)
            throw new IllegalArgumentException("Invalid amount");

        userDto.setAccountBalance(userDto.getAccountBalance() + amount);

        User user = userRepository.save(new User(userDto));

        return new UserDto(user);

    }
}
