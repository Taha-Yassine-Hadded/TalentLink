package com.platform.platformOffreM.controllers;

import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.findUserByUsername(principal.getName());
            model.addAttribute("user", user);
        }
        return "home";
    }

}
