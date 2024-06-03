package com.platform.platformOffreM.controllers;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.repositories.UserRepository;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class SignUpController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Value("${dir.imagesProfile}")
    private String imageDir;

    @GetMapping("/signUp")
    public String signUp(Model model) {
        Talent talent = new Talent();
        model.addAttribute("talent", talent);
        Entreprise entreprise = new Entreprise();
        model.addAttribute("entreprise", entreprise);
        return "SignUp/SignUp";
    }

    @PostMapping("/addTalent")
    public String saveTalent(@Valid Talent talent, BindingResult result, @ModelAttribute Entreprise entreprise, Model model) {
        model.addAttribute("talent", talent);

        Date date = talent.getDateNaissance();

        Date date1 = new Date();

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int year = localDate.getYear();

        int year1 = localDate1.getYear();


        if (userService.findUserByUsername(talent.getUsername()) != null) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "SignUp/SignUp";
        }

        if (year1 - year < 15) {
            model.addAttribute("errorDate", "L'âge minimum accepté est 15 ans");
            return "SignUp/SignUp";
        }

        if (userRepository.findByEmail(talent.getEmail()) != null) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "SignUp/SignUp";
        }


        if (result.hasErrors()) {
            return "SignUp/SignUp";
        }

        talent.setRole(roleService.findRoleByRoleName("TALENT"));
        userService.addUser(talent);
        return "redirect:/login";

    }

    @PostMapping("/addEntreprise")
    public String saveTalent(@Valid Entreprise entreprise, BindingResult result, @ModelAttribute Talent talent, Model model) {
        model.addAttribute("entreprise", entreprise);

        if (userService.findUserByUsername(entreprise.getUsername()) != null) {
            model.addAttribute("errorEntr", "utilisateur exist déja");
            return "SignUp/SignUp";
        }
        if (userRepository.findByEmail(entreprise.getEmail()) != null) {
            model.addAttribute("errorEmailEntr", "Email déja utilisé");
            return "SignUp/SignUp";
        }

        if (result.hasErrors()) {
            return "SignUp/SignUp";
        } else {
            entreprise.setRole(roleService.findRoleByRoleName("ENTREPRISE"));
            userService.addUser(entreprise);
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/getProfilePicture", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(String nomFile) throws Exception {
        File f = new File(imageDir + nomFile);
        return IOUtils.toByteArray(new FileInputStream(f));
    }
}