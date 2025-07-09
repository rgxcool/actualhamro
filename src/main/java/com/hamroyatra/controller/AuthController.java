package com.hamroyatra.controller;

import com.hamroyatra.model.User;
import com.hamroyatra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Login - HamroYatr++++++++a");
        model.addAttribute("activeTab", "login");
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register - HamroYatra");
        model.addAttribute("activeTab", "register");
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                              BindingResult result, 
                              Model model) {
        // Check for validation errors
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Register - HamroYatra");
            model.addAttribute("activeTab", "register");
            return "auth/register";
        }
        
        // Check if username is available
        if (!userService.isUsernameAvailable(user.getUsername())) {
            model.addAttribute("usernameError", "Username is already taken");
            model.addAttribute("pageTitle", "Register - HamroYatra");
            model.addAttribute("activeTab", "register");
            return "auth/register";
        }
        
        // Check if email is available
        if (!userService.isEmailAvailable(user.getEmail())) {
            model.addAttribute("emailError", "Email is already registered");
            model.addAttribute("pageTitle", "Register - HamroYatra");
            model.addAttribute("activeTab", "register");
            return "auth/register";
        }
        
        try {
            userService.registerNewUser(user);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("pageTitle", "Register - HamroYatra");
            model.addAttribute("activeTab", "register");
            return "auth/register";
        }
    }
}
