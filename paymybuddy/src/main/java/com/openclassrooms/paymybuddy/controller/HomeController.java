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

    @GetMapping("/connected_user")
    public String connectedUser(Model model, HttpServletRequest request) {
        User connectedUser = userRepository.findByUsername(request.getUserPrincipal().getName());
        model.addAttribute("connectedUser", connectedUser);
        return "connected_user";
    }

    @GetMapping("/connection_register")
    public String showConnectionRegistrationForm(Model model) {
        User user_2 = new User();
        model.addAttribute("user_2", user_2);
        return "connection_register_form";
    }
    @PostMapping("/process_connection_register")
    public String processConnectionRegister(User userToAdd, Model model, HttpServletRequest request) {
        User connectedUser = userRepository.findByUsername(request.getUserPrincipal().getName());
        userToAdd = userRepository.findById((userToAdd.getId()));
        if (userToAdd != null) {
            connectedUser.addConnection(userToAdd);
        }
        userRepository.save(connectedUser);
        model.addAttribute("connectedUser", connectedUser);
        return "connected_user";
    }
}
