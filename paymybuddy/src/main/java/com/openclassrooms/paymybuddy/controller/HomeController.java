package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.UserService;
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
    private UserService userService;

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
        userService.addUser(user);
        return "register_success";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User connectedUser = userService.findByUsername(principal.getName());
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
    public String processAddConnection(User connection, Model model, Principal principal) {
        User connectedUser = userService.findByUsername(principal.getName());
        connection = userService.findById((connection.getId()));
        if(userService.addConnectionToUser(connection, connectedUser)!=null) {
            model.addAttribute("connectedUser", connectedUser);
            return "profile";
        }
        return "error";
    }
}
