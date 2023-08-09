package com.openclassrooms.paymybuddy_testauth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, home!";
    }

    @GetMapping("/secured")
    public String secured(Principal user) {
        return "Hello, secured!";
    }
}
