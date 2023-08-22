package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        System.out.println("---user successfully saved");

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        User connectedUser = userRepository.findByUsername(request.getUserPrincipal().getName());
        model.addAttribute("connectedUser", connectedUser);
        return "profile";
    }

    @GetMapping("/add_connection")
    public String showAddConnectionForm(Model model) {
        User connection = new User();
        model.addAttribute("connection", connection);
        return "add_connection_form";
    }
    @PostMapping("/process_add_connection")
    public String processAddConnection(User connection, Model model, HttpServletRequest request) {
        User connectedUser = userRepository.findByUsername(request.getUserPrincipal().getName());
        connection = userRepository.findById((connection.getId()));
        if (connection != null) {
            connectedUser.addConnection(connection);
        }
        userRepository.save(connectedUser);
        model.addAttribute("connectedUser", connectedUser);
        return "connected_user";
    }
}
