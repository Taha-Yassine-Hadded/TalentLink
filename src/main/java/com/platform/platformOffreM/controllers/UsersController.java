package com.platform.platformOffreM.controllers;

import com.platform.platformOffreM.entities.Admin;
import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.repositories.UserRepository;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Controller
@RequestMapping("/admins")
public class UsersController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Value("${dir.imagesProfile}")
    private String imageDir;


    @GetMapping("/talentsList")
    public String TalentList(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size,
                             @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<User> pageTalents = userRepository.findByUsernameContainsAndRole_RoleName(keyword, "TALENT", PageRequest.of(page, size));

        model.addAttribute("listTalents", pageTalents.getContent());


        model.addAttribute("pages", new int[pageTalents.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "talentUsers";
    }


    @GetMapping("/entreprisesList")
    public String EntrepriseList(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size,
                                 @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<User> pageEntreprises = userRepository.findByUsernameContainsAndRole_RoleName(keyword, "ENTREPRISE", PageRequest.of(page, size));

        model.addAttribute("listEntreprises", pageEntreprises.getContent());
        model.addAttribute("pages", new int[pageEntreprises.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);


        return "entrepriseUsers";


    }


    @GetMapping("/adminsList")
    public String adminList(Principal principal, Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "5") int size,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<User> pageAdmins = userRepository.findByUsernameContainsAndRole_RoleName(keyword, "ADMIN", PageRequest.of(page, size));


        model.addAttribute("listAdmins", pageAdmins.getContent());
        model.addAttribute("pages", new int[pageAdmins.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("user", principal.getName());

        return "adminUsers";

    }


    @GetMapping("/blockTalent")
    public String blockTalent(Long id) {
        Talent talent = userService.findTalentById(id);
        talent.setEnabled(false);
        userRepository.save(talent);
        return "redirect:/admins/talentsList";
    }

    @GetMapping("/unblockTalent")
    public String unblockTalent(Long id) {
        Talent talent = userService.findTalentById(id);
        talent.setEnabled(true);
        userRepository.save(talent);
        return "redirect:/admins/talentsList";
    }


    @GetMapping("/blockEntreprise")
    public String blockEntreprise(Long id) {
        Entreprise entreprise = userService.findEntrepriseById(id);
        entreprise.setEnabled(false);
        userRepository.save(entreprise);
        return "redirect:/admins/entreprisesList";
    }

    @GetMapping("/unblockEntreprise")
    public String unblockEntreprise(Long id) {
        Entreprise entreprise = userService.findEntrepriseById(id);
        entreprise.setEnabled(true);
        userRepository.save(entreprise);
        return "redirect:/admins/entreprisesList";
    }


    @GetMapping("/deleteAdmin")
    public String deleteAdmin(Long id) {
        userService.deleteUserById(id);
        return "redirect:/admins/adminsList";
    }


    @GetMapping("/addTalent")
    public String addTalent(Model model) {
        model.addAttribute("talent", new Talent());
        return "formTalents";
    }

    @PostMapping("/saveTalent")
    public String saveTalent(@Valid Talent talent, BindingResult result, Model model) {
        model.addAttribute("talent", talent);

        Date date = talent.getDateNaissance();

        Date date1 = new Date();

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int year = localDate.getYear();

        int year1 = localDate1.getYear();


        if (userService.findUserByUsername(talent.getUsername()) != null) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "formTalents";
        }

        if (year1 - year < 15) {
            model.addAttribute("errorDate", "L'âge minimum accepté est 15 ans");
            return "formTalents";
        }

        if (userRepository.findByEmail(talent.getEmail()) != null) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "formTalents";
        }


        if (result.hasErrors()) {
            return "formTalents";
        }

        talent.setRole(roleService.findRoleByRoleName("TALENT"));
        userService.addUser(talent);
        return "redirect:/admins/talentsList";

    }


    @GetMapping("/addEntreprise")
    public String addEntreprise(Model model) {
        model.addAttribute("entreprise", new Entreprise());
        return "formEntreprises";
    }


    @PostMapping("/saveEntreprise")
    public String saveTalent(@Valid Entreprise entreprise, BindingResult result, Model model) {
        model.addAttribute("entreprise", entreprise);

        if (userService.findUserByUsername(entreprise.getUsername()) != null) {
            model.addAttribute("errorEntr", "utilisateur exist déja");
            return "formEntreprises";
        }
        if (userRepository.findByEmail(entreprise.getEmail()) != null) {
            model.addAttribute("errorEmailEntr", "Email déja utilisé");
            return "formEntreprises";
        }

        if (result.hasErrors()) {
            return "formEntreprises";
        } else {
            entreprise.setRole(roleService.findRoleByRoleName("ENTREPRISE"));
            userService.addUser(entreprise);
            return "redirect:/admins/entreprisesList";
        }
    }


    @GetMapping("/addAdmin")
    public String addAdmin(Model model) {
        model.addAttribute("admin", new Admin());
        return "formAdmins";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(@Valid Admin admin, BindingResult result, Model model) {
        model.addAttribute("admin", admin);

        if (userService.findUserByUsername(admin.getUsername()) != null) {
            model.addAttribute("errorEntr", "utilisateur exist déja");
            return "formAdmins";
        }
        if (userRepository.findByEmail(admin.getEmail()) != null) {
            model.addAttribute("errorEmailEntr", "Email déja utilisé");
            return "formAdmins";
        }

        if (result.hasErrors()) {
            return "formAdmins";
        }
        admin.setRole(roleService.findRoleByRoleName("ADMIN"));
        userService.addUser(admin);
        return "redirect:/admins/adminsList";
    }


    @GetMapping("/editTalent")
    public String editTalent(Model model, Long id) {
        Talent talent = userRepository.findTalentById(id);
        model.addAttribute("talent", talent);
        model.addAttribute("talent1", talent);
        return "editTalents";

    }

    @PostMapping("/saveTalentChanges")
    public String saveTalentChanges(Model model, @ModelAttribute Talent talent, @ModelAttribute Talent talent1, @RequestParam(name = "picture") MultipartFile file) throws IOException {
        model.addAttribute("talent", talent);
        model.addAttribute("talent1", talent1);

        Date date = talent.getDateNaissance();

        Date date1 = new Date();

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int year = localDate.getYear();

        int year1 = localDate1.getYear();


        if (talent.getNom().length() < 3) {
            model.addAttribute("errorNom", "Au moins 3 lettres pour le nom");
            return "editTalents";
        }

        if (talent.getPrenom().length() < 3) {
            model.addAttribute("errorPrenom", "Au moins 3 lettres pour le prénom");
            return "editTalents";
        }

        if (talent.getUsername().length() < 8) {
            model.addAttribute("errorUsername", "Au moins 8 lettres pour le nom d'utilisateur");
            return "editTalents";
        }
        String currentName = talent1.getUsername();
        String desiredName = talent.getUsername();
        if (userService.findUserByUsername(talent.getUsername()) != null && userService.findTalentByUsername(talent.getUsername()) != userService.findTalentById(talent1.getId())) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "editTalents";
        }

        if (year1 - year < 15) {
            model.addAttribute("errorDate", "L'âge minimum accepté est 15 ans");
            return "editTalents";
        }

        if (userRepository.findByEmail(talent.getEmail()) != null && userService.findTalentByEmail(talent.getEmail()) != userService.findTalentById(talent1.getId())) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "editTalents";
        }



        if (!file.isEmpty()) {
            String nomFile = file.getOriginalFilename() + UUID.randomUUID().toString();
            talent.setProfilePicture(nomFile);
            file.transferTo(new File(imageDir + nomFile));
        }
        userService.updateTalentById(talent1.getId(), talent);
        return "redirect:/admins/talentsList";
    }


    @GetMapping("/editEntreprise")
    public String editEntreprise(Model model, Long id) {
        Entreprise entreprise = userRepository.findEntrepriseById(id);
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("entreprise1", entreprise);
        return "editEntreprises";

    }

    @PostMapping("/saveEntrepriseChanges")
    public String saveEntrepriseChanges(Model model, @ModelAttribute Entreprise entreprise, @ModelAttribute Entreprise entreprise1) {
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("entreprise1", entreprise1);

        if (entreprise.getNom().length() < 3) {
            model.addAttribute("errorNom", "Au moins 3 lettres pour le nom");
            return "editEntreprises";
        }

        if (entreprise.getUsername().length() < 4) {
            model.addAttribute("errorUsername", "Au moins 4 lettres pour le nom d'utilisateur");
            return "editEntreprises";
        }


        if (userService.findUserByUsername(entreprise.getUsername()) != null && userService.findEntrepriseByUsername(entreprise.getUsername()) != userService.findEntrepriseById(entreprise1.getId())) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "editEntreprises";
        }

        if (userRepository.findByEmail(entreprise.getEmail()) != null && userRepository.findEntrepriseByEmail(entreprise.getEmail()) != userService.findEntrepriseById(entreprise1.getId())) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "editEntreprises";
        }


        userService.updateEntrepriseById(entreprise1.getId(), entreprise);
        return "redirect:/admins/entreprisesList";
    }

    @GetMapping("/editAdmin")
    public String editAdmin(Model model, Long id) {
        Admin admin = userRepository.findAdminById(id);
        model.addAttribute("admin", admin);
        model.addAttribute("admin1", admin);
        return "editAdmins";

    }

    @PostMapping("/saveAdminChanges")
    public String saveAdminChanges(HttpServletRequest request, Principal principal, Model model, @ModelAttribute Admin admin, @ModelAttribute Admin admin1) {
        model.addAttribute("admin", admin);
        model.addAttribute("admin1", admin1);

        String newPassword = request.getParameter("newPassword");

        if (admin.getNom().length() < 3) {
            model.addAttribute("errorNom", "Au moins 3 lettres pour le nom");
            return "editAdmins";
        }

        if (admin.getUsername().length() < 4) {
            model.addAttribute("errorUsername", "Au moins 4 lettres pour le nom d'utilisateur");
            return "editAdmins";
        }

        if (userService.findUserByUsername(admin.getUsername()) != null && userService.findAdminByUsername(admin.getUsername()) != userService.findAdminById(admin1.getId())) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "editAdmins";
        }

        if (userRepository.findByEmail(admin.getEmail()) != null && userService.findAdminByEmail(admin.getEmail()) != userService.findAdminById(admin1.getId())) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "editAdmins";
        }

        if (newPassword.length() < 4 && !newPassword.equals("")) {
            model.addAttribute("errorPassword", "Au moins 4 lettres pour le mot de passe");
            return "editAdmins";
        }

        if (userService.findAdminById(admin1.getId()) == userService.findAdminByUsername(principal.getName()) && admin.getUsername().equals(principal.getName())) {
            if (newPassword.equals(""))
                userService.updateAdminById(admin1.getId(), admin);

            else {
                BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder1.encode(newPassword);
                userService.updateAdminById(admin1.getId(), admin);

                admin1.setPassword(encodePassword);
                admin1.setRole(roleService.findRoleByRoleName("ADMIN"));
                userRepository.save(admin1);
            }

            return "redirect:/admins/adminsList";
        }

        if (userService.findAdminById(admin1.getId()) == userService.findAdminByUsername(principal.getName()) && !admin.getUsername().equals(principal.getName())) {

            if (newPassword.equals(""))
                userService.updateAdminById(admin1.getId(), admin);

            else {
                BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
                String encodePassword = passwordEncoder1.encode(newPassword);
                userService.updateAdminById(admin1.getId(), admin);

                admin1.setPassword(encodePassword);
                admin1.setRole(roleService.findRoleByRoleName("ADMIN"));
                userRepository.save(admin1);
            }
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "redirect:/login";
        }
        userService.updateAdminById(admin1.getId(), admin);
        return "redirect:/admins/adminsList";
    }

    @GetMapping("/deleteProfilPhoto")
    public String deleteProfilPhoto(Long id) {

        Talent talent = userService.findTalentById(id);
        talent.setProfilePicture(null);
        userRepository.save(talent);
        Long talentId = talent.getId();

        return "redirect:/admins/editTalent?id=" + talentId;
    }

}
