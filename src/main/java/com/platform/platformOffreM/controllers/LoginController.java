package com.platform.platformOffreM.controllers;

import com.platform.platformOffreM.entities.Admin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String LoginPage(Model model) {
        model.addAttribute("user", new Admin());
        return "login";
    }

}
