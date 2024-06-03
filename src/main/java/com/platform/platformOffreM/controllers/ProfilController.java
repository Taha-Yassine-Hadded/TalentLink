package com.platform.platformOffreM.controllers;


import com.platform.platformOffreM.entities.*;
import com.platform.platformOffreM.repositories.FollowRepository;
import com.platform.platformOffreM.repositories.NotificationRepository;
import com.platform.platformOffreM.repositories.OffreDeMissionRepository;
import com.platform.platformOffreM.repositories.UserRepository;
import com.platform.platformOffreM.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.time.LocalDate;
import java.time.Period;


@Controller
@RequestMapping("/profils")
public class ProfilController {


    @Value("${dir.imagesProfile}")
    private String imageDir;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OffreDeMissionRepository offreDeMissionRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OffreDeMissionService offreDeMissionService;

    @Autowired
    private CandidatureService candidatureService;


    @GetMapping("/myProfilT")
    public String getTalentProfil(Principal principal, Model model) {
        Talent talent = new Talent();
        talent = userService.findTalentByUsername(principal.getName());
        model.addAttribute("talent", talent);

        String birthdateString = talent.getDateNaissance().toString();
        LocalDate birthdate = LocalDate.parse(birthdateString);

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);
        int age = period.getYears();

        model.addAttribute("age", age);

        return "profils/myProfilT";

    }

    @GetMapping("/myProfilE")
    public String getEntrepriseProfil(Principal principal, Model model) {
        Entreprise entreprise = new Entreprise();
        entreprise = userService.findEntrepriseByUsername(principal.getName());
        model.addAttribute("entreprise", entreprise);

        List<OffreDeMission> entrepriseOffres = new ArrayList<>();
        entrepriseOffres = offreDeMissionService.findOffreByEntrepriseID(entreprise.getId());
        List<Integer> candidatureCounts = new ArrayList<>();
        for (OffreDeMission offre : entrepriseOffres) {
            int count = candidatureService.countCandidatureByOffreDeMission(offre.getId());
            candidatureCounts.add(count);
        }
        model.addAttribute("candidatureCounts", candidatureCounts);

        int countOffres = offreDeMissionRepository.countByEntreprise(entreprise);

        model.addAttribute("entrepriseOffres", entrepriseOffres);
        model.addAttribute("countOffres", countOffres);

        List<Follow> allFollowers = followRepository.findAll();
        int count = followRepository.countByEntreprise(entreprise);
        model.addAttribute("count", count);


        return "profils/myProfilE";
    }

    @GetMapping("/talentProfil")
    public String getTalentProfil(Model model, Long id) {

        Talent talent = new Talent();
        talent = userService.findTalentById(id);
        model.addAttribute("talent", talent);


        String birthdateString = talent.getDateNaissance().toString();
        LocalDate birthdate = LocalDate.parse(birthdateString);

        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);
        int age = period.getYears();

        model.addAttribute("age", age);

        return "profils/profilTalent";

    }


    @GetMapping("/entrepriseProfil")
    public String getEntrepriseProfil(Authentication authentication, Principal principal, Model model, Long id) {

        Entreprise entreprise = new Entreprise();
        entreprise = userService.findEntrepriseById(id);
        model.addAttribute("entreprise", entreprise);

        List<OffreDeMission> entrepriseOffres = new ArrayList<>();
        entrepriseOffres = offreDeMissionService.findOffreByEntrepriseID(entreprise.getId());
        List<Integer> candidatureCounts = new ArrayList<>();
        for (OffreDeMission offre : entrepriseOffres) {
            int count = candidatureService.countCandidatureByOffreDeMission(offre.getId());
            candidatureCounts.add(count);
        }
        model.addAttribute("candidatureCounts", candidatureCounts);

        int countOffres = offreDeMissionRepository.countByEntreprise(entreprise);

        model.addAttribute("entrepriseOffres", entrepriseOffres);
        model.addAttribute("countOffres", countOffres);

        List<Follow> allFollowers = followRepository.findAll();
        int count = followRepository.countByEntreprise(entreprise);
        model.addAttribute("count", count);

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("TALENT"))) {
                    Talent talent = new Talent();
                    talent = userService.findTalentByUsername(principal.getName());
                    model.addAttribute("talent", talent);

                    boolean b = false;
                    if (followRepository.findByTalentAndEntreprise(talent, entreprise) != null)
                        b = true;
                    model.addAttribute("b", b);

                }
            }
        }
        return "profils/profilEntreprise";

    }

    @GetMapping("/follow")
    public String followEntreprise(Principal principal, Model model, Long id) {
        Talent talent = new Talent();
        talent = userService.findTalentByUsername(principal.getName());

        Entreprise entreprise = new Entreprise();
        entreprise = userService.findEntrepriseById(id);


        followService.subscribe(talent, entreprise);

        String message = String.format(" %s a commencé de vous suivre !", talent.getUsername());

        Notification notification = new Notification();
        notification.setTalent(userService.findTalentByUsername(principal.getName()));
        notification.setEntreprise(entreprise);
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notification.setSeen(false);
        notificationRepository.save(notification);


        return "redirect:/profils/entrepriseProfil?id=" + id;
    }

    @GetMapping("/unfollow")
    public String unfollowEntreprise(Principal principal, Model model, Long id) {
        Talent talent = new Talent();
        talent = userService.findTalentByUsername(principal.getName());

        Entreprise entreprise = new Entreprise();
        entreprise = userService.findEntrepriseById(id);

        followService.unsubscribe(talent, entreprise);

        return "redirect:/profils/entrepriseProfil?id=" + id;
    }


    @GetMapping("/updateTalent")
    public String updateTalent(Principal principal, Model model) {
        Talent talent = userRepository.findTalentByUsername(principal.getName());
        model.addAttribute("talent", talent);
        model.addAttribute("talent1", talent);
        return "updateTalent";

    }

    @PostMapping("/saveTalentUpdates")
    public String saveUpdates(HttpServletRequest request, Principal principal, Model model, @ModelAttribute Talent talent, @ModelAttribute Talent talent1, @RequestParam(name = "picture") MultipartFile file) throws IOException {
        model.addAttribute("talent", talent);
        model.addAttribute("talent1", talent1);

        String genre = talent1.getGenre();

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        Date date = talent.getDateNaissance();

        Date date1 = new Date();

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int year = localDate.getYear();

        int year1 = localDate1.getYear();


        if (talent.getNom().length() < 3) {
            model.addAttribute("errorNom", "Au moins 3 lettres pour le nom");
            return "updateTalent";
        }

        if (talent.getPrenom().length() < 3) {
            model.addAttribute("errorPrenom", "Au moins 3 lettres pour le prénom");
            return "updateTalent";
        }

        if (talent.getUsername().length() < 4) {
            model.addAttribute("errorUsername", "Au moins 4 lettres pour le nom d'utilisateur");
            return "updateTalent";
        }

        if (userService.findUserByUsername(talent.getUsername()) != null && userService.findUserByUsername(talent.getUsername()) != userService.findUserByUsername(principal.getName())) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "updateTalent";
        }

        if (year1 - year < 15) {
            model.addAttribute("errorDate", "L'âge minimum accepté est 15 ans");
            return "updateTalent";
        }

        if (userRepository.findByEmail(talent.getEmail()) != null && userRepository.findByEmail(talent.getEmail()) != userService.findTalentByUsername(principal.getName())) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "updateTalent";
        }

        if (newPassword.length() < 8 && !newPassword.equals("")) {
            model.addAttribute("errorPassword", "Au moins 8 lettres pour le mot de passe");
            return "updateTalent";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorPassword", "Confirmez avec le même mot de passe !");
            return "updateTalent";
        }


        if (!file.isEmpty()) {
            String nomFile = file.getOriginalFilename() + UUID.randomUUID().toString();
            talent.setProfilePicture(nomFile);
            file.transferTo(new File(imageDir + nomFile));
        }


        if (newPassword.equals(""))
            userService.updateTalentById(talent1.getId(), talent);
        else {
            BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
            String encodePassword = passwordEncoder1.encode(newPassword);
            userService.updateTalentById(talent1.getId(), talent);

            talent1.setPassword(encodePassword);
            talent1.setRole(roleService.findRoleByRoleName("TALENT"));
            userRepository.save(talent1);
        }

        if (talent.getUsername().equals(principal.getName()))
            return "redirect:/profils/myProfilT";
        else {
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "redirect:/login";
        }

    }


    @GetMapping("/updateEntreprise")
    public String updateEntreprise(Principal principal, Model model) {
        Entreprise entreprise = userRepository.findEntrepriseByUsername(principal.getName());
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("entreprise1", entreprise);
        return "updateEntreprise";

    }

    @PostMapping("/saveEntrepriseUpdates")
    public String saveEntrepriseUpdates(HttpServletRequest request, Principal principal, Model model, @ModelAttribute Entreprise entreprise, @ModelAttribute Entreprise entreprise1) {
        model.addAttribute("entreprise", entreprise);
        model.addAttribute("entreprise1", entreprise1);

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (entreprise.getNom().length() < 3) {
            model.addAttribute("errorNom", "Au moins 3 lettres pour le nom");
            return "updateEntreprise";
        }

        if (entreprise.getUsername().length() < 4) {
            model.addAttribute("errorUsername", "Au moins 4 lettres pour le nom d'utilisateur");
            return "updateEntreprise";
        }


        if (userService.findUserByUsername(entreprise.getUsername()) != null && userService.findUserByUsername(entreprise.getUsername()) != userService.findUserByUsername(principal.getName())) {
            model.addAttribute("errorTal", "utilisateur exist déja");
            return "updateEntreprise";
        }


        if (userRepository.findByEmail(entreprise.getEmail()) != null && userRepository.findByEmail(entreprise.getEmail()) != userService.findEntrepriseByUsername(principal.getName())) {
            model.addAttribute("errorEmailTal", "Email déja utilisé");
            return "updateEntreprise";
        }

        if (newPassword.length() < 8 && !newPassword.equals("")) {
            model.addAttribute("errorPassword", "Au moins 8 lettres pour le mot de passe");
            return "updateEntreprise";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorPassword", "Confirmez avec la même mot de passe !");
            return "updateEntreprise";
        }



        if (newPassword.equals(""))
            userService.updateEntrepriseById(entreprise1.getId(), entreprise);
        else {
            BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
            String encodePassword = passwordEncoder1.encode(newPassword);
            userService.updateEntrepriseById(entreprise1.getId(), entreprise);

            entreprise1.setPassword(encodePassword);
            entreprise1.setRole(roleService.findRoleByRoleName("ENTREPRISE"));
            userRepository.save(entreprise1);
        }

        if (entreprise.getUsername().equals(principal.getName()))
            return "redirect:/profils/myProfilE";
        else {
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "redirect:/login";
        }
    }

    @GetMapping("/deleteProfilPicture")
    public String deleteProfilPicture(Long id) {

        Talent talent = userService.findTalentById(id);
        talent.setProfilePicture(null);
        userRepository.save(talent);

        return "redirect:/profils/updateTalent";
    }


}
