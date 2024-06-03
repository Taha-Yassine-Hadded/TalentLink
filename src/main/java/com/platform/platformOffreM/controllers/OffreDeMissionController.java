package com.platform.platformOffreM.controllers;


import com.platform.platformOffreM.entities.*;
import com.platform.platformOffreM.entities.CvInfos.Competence;

import com.platform.platformOffreM.repositories.CandidatureRepository;
import com.platform.platformOffreM.repositories.NotificationRepository;
import com.platform.platformOffreM.repositories.OffreDeMissionRepository;
import com.platform.platformOffreM.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/offres")
public class OffreDeMissionController {

    @Autowired
    private FollowService followService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private OffreDeMissionService offreDeMissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompetenceService competenceService;

    @Autowired
    private CandidatureService candidatureService;

    private OffreDeMission offreDeMission = new OffreDeMission();
    private Competence competence = new Competence();
    private List<Competence> competenceList = new ArrayList<>();
    private List<Competence> oldCompetenceList = new ArrayList<>();
    private List<Candidature> candidatureList = new ArrayList<>();
    @Autowired
    private OffreDeMissionRepository offreDeMissionRepository;

    @GetMapping
    public String getAllOffre(Authentication authentication, Principal principal, Model model) {

        model.addAttribute("username", principal.getName());
        List<OffreDeMission> allOffres = offreDeMissionService.findAllOffre();
        model.addAttribute("allOffres", allOffres);


        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ENTREPRISE"))) {
                    Entreprise entreprise = new Entreprise();
                    entreprise = userService.findEntrepriseByUsername(principal.getName());
                    List<OffreDeMission> myOffres = offreDeMissionService.findOffreByEntrepriseID(entreprise.getId());
                    model.addAttribute("myOffres", myOffres);
                }
            }
        }

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("TALENT"))) {
                    Talent talent = userService.findTalentByUsername(principal.getName());
                    List<Candidature> candidatList = candidatureRepository.findByTalent_Id(talent.getId());
                    model.addAttribute("candidatList", candidatList);
                }
            }
        }


        return "allOffres";
    }

    @GetMapping("/add")
    public String addOffre(Model model) {
        this.offreDeMission = new OffreDeMission();
        this.competence = new Competence();
        this.competenceList = new ArrayList<>();
        model.addAttribute("offre", this.offreDeMission);
        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);
        return "offreDeMission";
    }


    @PostMapping("/save")
    public String saveOffre(Principal principal, @ModelAttribute OffreDeMission offre, RedirectAttributes redirectAttributes, Model model) {

        Set<Competence> competences = new HashSet<>(this.competenceList);

        if (competences.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ajoutez au moins une compétence !");
            return "redirect:/offres/add";
        } else {
            String entreprise = principal.getName();
            offre.setEntreprise(userService.findEntrepriseByUsername(entreprise));
            offre.setCompetences(competences);
            offre.setDate(new Date());
            offreDeMissionService.addOffre(offre);
            OffreDeMission savedOffreDeMission = offre;
            Entreprise entrep = userService.findEntrepriseByUsername(entreprise);
            List<Talent> talents = followService.findTalentsByEntreprise(entrep);
            String message = String.format("L'entreprise %s a ajouté une nouvelle offre de mission", entrep.getNom());
            for (Talent talent : talents) {
                Notification notification = new Notification();
                notification.setTalent(talent);
                notification.setEntreprise(null);
                notification.setOffreDeMission(savedOffreDeMission);
                notification.setMessage(message);
                notification.setDate(LocalDateTime.now());
                notification.setSeen(false);
                notificationRepository.save(notification);
            }
            return "redirect:/offres";
        }
    }


    @PostMapping("/saveCompetence")
    public String addCompetence(@ModelAttribute Competence competence, Model model) {
        this.competenceList.add(competence);
        this.competence = new Competence();
        model.addAttribute("offre", this.offreDeMission);
        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);

        return "offreDeMission";
    }

    @PostMapping("/newCompetence")
    public String newCompetence(@ModelAttribute Competence competence, Model model) {
        this.competenceList.add(competence);
        this.competence = new Competence();
        model.addAttribute("offre", this.offreDeMission);
        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editOffre";
    }

    @GetMapping("/subscribe")
    public String subscribeOffre(Principal principal, Long id) {
        Candidature candidature = new Candidature();
        candidature.setTalent(userService.findTalentByUsername(principal.getName()));
        candidature.setOffreDeMission(offreDeMissionService.findOffreById(id));
        candidature.setStatus("PENDING");
        candidature.setDate(new Date());
        candidatureService.addCondidature(candidature);

        String message = String.format(" %s a envoyé une demande pour l'offre %s ", principal.getName(), offreDeMissionService.findOffreById(id).getTitre());

        Notification notification = new Notification();
        notification.setTalent(userService.findTalentByUsername(principal.getName()));
        notification.setEntreprise(offreDeMissionService.findOffreById(id).getEntreprise());
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notification.setSeen(false);
        notificationRepository.save(notification);

        OffreDeMission offre = offreDeMissionService.findOffreById(id);
        Long offreId = offre.getId();

        return "redirect:/offres/offreInfo?id=" + offreId;
    }

    @GetMapping("/delete")
    public String deleteOffre(HttpServletRequest request, Principal principal, Model model, Long id) {

        Long entrepriseId = offreDeMissionService.findOffreById(id).getEntreprise().getId();


        offreDeMissionService.deleteOffre(id);

        return "redirect:/offres";
    }


    @GetMapping("/cancelSubscribe")
    public String cancelSubscribe(Principal principal, Long id) {
        Talent talent = userService.findTalentByUsername(principal.getName());
        candidatureService.deleteCandidatureByTalent_IdAndOffreDeMission_Id(talent.getId(), id);

        OffreDeMission offreDeMission1 = offreDeMissionService.findOffreById(id);
        Long offreId = offreDeMission1.getId();

        return "redirect:/offres/offreInfo?id=" + offreId;
    }

    @GetMapping("/deleteCandidature")
    public String deleteCandidature(Long id) {
        Candidature candidature = candidatureService.findCondidatureById(id);
        Long offreId = candidature.getOffreDeMission().getId();

        candidatureRepository.deleteById(id);

        return "redirect:/offres/offreInfo?id=" + offreId;
    }

    @GetMapping("/offreInfo")
    public String moreOffreInfo(Authentication authentication, Principal principal, Model model, Long id) {

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                    List<Candidature> candidatList = candidatureRepository.findByOffreDeMission_Id(id);
                    model.addAttribute("candidatList", candidatList);
                }
            }
        }

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ENTREPRISE"))) {
                    List<Candidature> candidatList = candidatureRepository.findByOffreDeMission_Id(id);
                    model.addAttribute("candidatList", candidatList);
                }
            }
        }

        model.addAttribute("username", principal.getName());
        OffreDeMission offre = offreDeMissionService.findOffreById(id);

        model.addAttribute("offre", offre);

        int count = candidatureService.countCandidatureByOffreDeMission(id);
        model.addAttribute("count", count);

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("TALENT"))) {
                    Talent talent = new Talent();
                    talent = userService.findTalentByUsername(principal.getName());

                    Candidature candidature = new Candidature();
                    candidature = candidatureService.findCandidatureByTalentAndOffre(talent.getId(), id);
                    if (candidature != null) {
                        model.addAttribute("abbone", "true");
                        model.addAttribute("candidature", candidature);
                    } else {
                        model.addAttribute("abbone", "false");
                    }
                }
            }
        }
        return "offreDeMissionInfo";
    }

    @GetMapping("/acceptCandidature")
    public String acceptCandidature(Long id) {
        candidatureService.findCondidatureById(id).setStatus("ACCEPTED");


        OffreDeMission savedOffreDeMission = candidatureService.findCondidatureById(id).getOffreDeMission();
        Entreprise entrep = candidatureService.findCondidatureById(id).getOffreDeMission().getEntreprise();
        Talent talent = candidatureService.findCondidatureById(id).getTalent();
        String message = String.format("L'entreprise %s a accepté votre demande pour l'offre de mission %s", entrep.getNom(), savedOffreDeMission.getTitre());

        Notification notification = new Notification();
        notification.setTalent(talent);
        notification.setEntreprise(null);
        notification.setOffreDeMission(savedOffreDeMission);
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notification.setSeen(false);
        notificationRepository.save(notification);

        Long idOffre = candidatureService.findCondidatureById(id).getOffreDeMission().getId();
        return "redirect:/offres/offreInfo?id=" + idOffre;
    }

    @GetMapping("/rejectCandidature")
    public String rejectCandidature(Long id) {
        candidatureService.findCondidatureById(id).setStatus("REJECTED");

        OffreDeMission savedOffreDeMission = candidatureService.findCondidatureById(id).getOffreDeMission();
        Entreprise entrep = candidatureService.findCondidatureById(id).getOffreDeMission().getEntreprise();
        Talent talent = candidatureService.findCondidatureById(id).getTalent();
        String message = String.format("L'entreprise %s a rejecté votre demande pour l'offre de mission %s", entrep.getNom(), savedOffreDeMission.getTitre());

        Notification notification = new Notification();
        notification.setTalent(talent);
        notification.setEntreprise(null);
        notification.setOffreDeMission(savedOffreDeMission);
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notification.setSeen(false);
        notificationRepository.save(notification);

        Long idOffre = candidatureService.findCondidatureById(id).getOffreDeMission().getId();
        return "redirect:/offres/offreInfo?id=" + idOffre;
    }


    @GetMapping("/saveOffreChange")
    public String saveOffreChange(RedirectAttributes redirectAttributes, Model model, @ModelAttribute OffreDeMission offre) {

        Long offreId = this.offreDeMission.getId();

        if (this.oldCompetenceList.isEmpty() && this.competenceList.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ajoutez au moins une compétence !");
            return "redirect:/offres/edit?id=" + offreId;
        }

        for (Competence competence : this.competenceList) {
            competence.setOffreDeMission(this.offreDeMission);
            competenceService.addCompetence(competence);
        }


        offreDeMissionService.updateOffre(this.offreDeMission.getId(), offre);


        return "redirect:/offres/offreInfo?id=" + offreId;
    }


    @GetMapping("/deleteCompetence")
    public String deleteCompetence(Model model, int i) {
        this.competenceList.remove(i);

        model.addAttribute("offre", this.offreDeMission);


        model.addAttribute("competenceList", this.competenceList);


        this.competence = new Competence();

        model.addAttribute("competence", this.competence);


        return "offreDeMission";
    }

    @GetMapping("/edit")
    public String editOffre(Model model, Long id) {

        this.offreDeMission = offreDeMissionService.findOffreById(id);
        this.competence = new Competence();

        this.oldCompetenceList = new ArrayList<>(this.offreDeMission.getCompetences());

        this.competenceList = new ArrayList<>();

        model.addAttribute("offre", this.offreDeMission);

        model.addAttribute("competence", this.competence);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editOffre";

    }

    @GetMapping("/deleteOldCompetence")
    public String deleteOldCompetence(Model model, Long id) {

        OffreDeMission offre = new OffreDeMission();
        offre = offreDeMissionService.findOffreById(this.offreDeMission.getId());
        offre.getCompetences().remove(competenceService.findCompetenceById(id));
        competenceService.deleteCompetence(id);
        offreDeMissionService.addOffre(offre);

        this.offreDeMission = offre;

        model.addAttribute("offre", this.offreDeMission);


        model.addAttribute("competenceList", this.competenceList);

        this.competence = new Competence();

        model.addAttribute("competence", this.competence);

        this.oldCompetenceList = new ArrayList<>(this.offreDeMission.getCompetences());
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editOffre";
    }

    @GetMapping("/deleteNewCompetence")
    public String deleteNewCompetence(Model model, int i) {
        this.competenceList.remove(i);

        model.addAttribute("offre", this.offreDeMission);


        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);


        this.competence = new Competence();

        model.addAttribute("competence", this.competence);


        return "editOffre";
    }


}
