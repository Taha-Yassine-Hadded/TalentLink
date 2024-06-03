package com.platform.platformOffreM.controllers;


import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.DocumentException;
import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.*;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.repositories.CvRepository;
import com.platform.platformOffreM.services.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;


import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/cv")
public class CvController {

    @Value("${dir.imagesCv}")
    private String imageDir;

    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
    private CvService cvService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompetenceService competenceService;

    @Autowired
    private FormationService formationService;

    @Autowired
    private ExperienceService experienceService;

    @Autowired
    private LangueService langueService;

    @Autowired
    private ContactService contactService;

    private Cv cv = new Cv();

    private Contact contact = new Contact();
    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> oldContactList = new ArrayList<>();

    private Competence competence = new Competence();
    private List<Competence> competenceList = new ArrayList<>();
    private List<Competence> oldCompetenceList = new ArrayList<>();

    private Formation formation = new Formation();
    private List<Formation> formationList = new ArrayList<>();
    private List<Formation> oldFormationList = new ArrayList<>();

    private Experience experience = new Experience();
    private List<Experience> experienceList = new ArrayList<>();
    private List<Experience> oldExperienceList = new ArrayList<>();

    private Langue langue = new Langue();
    private List<Langue> langueList = new ArrayList<>();
    private List<Langue> oldLangueList = new ArrayList<>();
    @Autowired
    private CvRepository cvRepository;


    @GetMapping("/add")
    public String addCv(Model model) {
        this.cv = new Cv();

        this.contact = new Contact();
        this.contactList = new ArrayList<>();

        this.competence = new Competence();
        this.competenceList = new ArrayList<>();

        this.formation = new Formation();
        this.formationList = new ArrayList<>();

        this.experience = new Experience();
        this.experienceList = new ArrayList<>();

        this.langue = new Langue();
        this.langueList = new ArrayList<>();


        model.addAttribute("cv", this.cv);

        model.addAttribute("contact", this.contact);
        model.addAttribute("contactList", this.contactList);

        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formation", this.formation);
        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experience", this.experience);
        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("langue", this.langue);
        model.addAttribute("langueList", this.langueList);

        return "cv";
    }


    @PostMapping("/save")
    public String saveCv(RedirectAttributes redirectAttributes, Principal principal, @ModelAttribute Cv cv, Model model, @RequestParam(name = "picture") MultipartFile file) throws IOException {


        if (this.contactList.isEmpty() && this.competenceList.isEmpty() && this.formationList.isEmpty() && this.experienceList.isEmpty() && this.langueList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorAll", "Ajoutez au moins un contact, une compétence, une formation, une expérience et une langue !");
            return "redirect:/cv/add";
        }

        if (this.contactList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorContact", "Ajoutez au moins un contact !");
            return "redirect:/cv/add";
        }

        if (this.competenceList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCompetence", "Ajoutez au moins une compétence !");
            return "redirect:/cv/add";
        }

        if (this.formationList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorFormation", "Ajoutez au moins une formation !");
            return "redirect:/cv/add";
        }

        if (this.experienceList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorExperience", "Ajoutez au moins une éxpérience !");
            return "redirect:/cv/add";
        }

        if (this.langueList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorLangue", "Ajoutez au moins une langue !");
            return "redirect:/cv/add";
        }


        String talentUsername = principal.getName();
        Talent talent = new Talent();
        talent = userService.findTalentByUsername(talentUsername);
        cv.setTalent(userService.findTalentByUsername(talentUsername));

        Set<Contact> contacts = new HashSet<>(this.contactList);
        cv.setContacts(contacts);

        Set<Competence> competences = new HashSet<>(this.competenceList);
        cv.setCompetences(competences);

        Set<Formation> formations = new HashSet<>(this.formationList);
        cv.setFormations(formations);

        Set<Experience> experiences = new HashSet<>(this.experienceList);
        cv.setExperiences(experiences);

        Set<Langue> langues = new HashSet<>(this.langueList);
        cv.setLangues(langues);

        if (!file.isEmpty()) {
            String nomFile = file.getOriginalFilename() + UUID.randomUUID().toString();
            cv.setCvPicture(nomFile);
            cvService.addCv(cv);
            file.transferTo(new File(imageDir + nomFile));
        } else {
            cv.setCvPicture(null);
            cvService.addCv(cv);
        }

        return "redirect:/profils/myProfilT";
    }

    @PostMapping("/saveContact")
    public String addContact(@ModelAttribute Competence competence, @ModelAttribute Contact contact, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.contactList.add(contact);
        this.contact = new Contact();
        model.addAttribute("cv", this.cv);
        model.addAttribute("contact", this.contact);
        model.addAttribute("contactList", this.contactList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("langueList", this.langueList);


        return "cv";
    }

    @PostMapping("/saveCompetence")
    public String addCompetence(@ModelAttribute Competence competence, @ModelAttribute Contact contact, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.competenceList.add(competence);
        this.competence = new Competence();
        model.addAttribute("cv", this.cv);
        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("langueList", this.langueList);


        return "cv";
    }

    @PostMapping("/saveFormation")
    public String addFormation(@ModelAttribute Competence competence, @ModelAttribute Contact contact, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.formationList.add(formation);
        this.formation = new Formation();
        model.addAttribute("cv", this.cv);
        model.addAttribute("formation", this.formation);
        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("langueList", this.langueList);

        return "cv";
    }


    @PostMapping("/saveExperience")
    public String addExperience(@ModelAttribute Competence competence, @ModelAttribute Contact contact, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.experienceList.add(experience);
        this.experience = new Experience();
        model.addAttribute("cv", this.cv);
        model.addAttribute("experience", this.experience);
        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("langueList", this.langueList);

        return "cv";
    }

    @PostMapping("/saveLangue")
    public String addLangue(@ModelAttribute Competence competence, @ModelAttribute Contact contact, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.langueList.add(langue);
        this.langue = new Langue();

        model.addAttribute("cv", this.cv);
        model.addAttribute("langue", this.langue);
        model.addAttribute("langueList", this.langueList);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        return "cv";
    }


    @PostMapping("/saveCvChanges")
    public String saveCvChanges(RedirectAttributes redirectAttributes, Principal principal, Model model, @ModelAttribute Cv cv, @RequestParam(name = "picture") MultipartFile file) throws IOException {

        if (this.contactList.isEmpty() && this.oldContactList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorContact", "Ajoutez au moins un contact !");
            return "redirect:/cv/edit";
        }

        if (this.competenceList.isEmpty() && this.oldCompetenceList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorCompetence", "Ajoutez au moins une compétence !");
            return "redirect:/cv/edit";
        }

        if (this.formationList.isEmpty() && this.oldFormationList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorFormation", "Ajoutez au moins une formation !");
            return "redirect:/cv/edit";
        }

        if (this.experienceList.isEmpty() && this.oldExperienceList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorExperience", "Ajoutez au moins une éxpérience !");
            return "redirect:/cv/edit";
        }

        if (this.langueList.isEmpty() && this.oldLangueList.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorLangue", "Ajoutez au moins une langue !");
            return "redirect:/cv/edit";
        }


        Talent talent = new Talent();
        talent = userService.findTalentByUsername(principal.getName());

        for (Contact contact1 : this.contactList) {
            contact1.setCv(talent.getCv());
            contactService.addContact(contact1);
        }

        for (Formation formation1 : this.formationList) {
            formation1.setCv(talent.getCv());
            formationService.addFormation(formation1);
        }

        for (Competence competence1 : this.competenceList) {
            competence1.setCv(talent.getCv());
            competenceService.addCompetence(competence1);
        }

        for (Experience experience1 : this.experienceList) {
            experience1.setCv(talent.getCv());
            experienceService.addExperience(experience1);
        }

        for (Langue langue1 : this.langueList) {
            langue1.setCv(talent.getCv());
            langueService.addLangue(langue1);
        }

        if (!file.isEmpty()) {
            String nomFile = file.getOriginalFilename() + UUID.randomUUID().toString();
            talent.getCv().setCvPicture(nomFile);
            file.transferTo(new File(imageDir + nomFile));
        }

        cvService.updateCv(talent.getCv().getId(), cv);

        return "redirect:/profils/myProfilT";
    }

    @GetMapping("/delete")
    public String deleteCv(Principal principal, Model model, Long id) {
        Talent talent = new Talent();
        talent = userService.findTalentByUsername(principal.getName());
        cvService.deleteCv(id);
        return "redirect:/profils/myProfilT";
    }

    @GetMapping("/deleteContact")
    public String deleteContact(Model model, int i) {
        this.contactList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);


        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "cv";
    }

    @GetMapping("/deleteCompetence")
    public String deleteCompetence(Model model, int i) {
        this.competenceList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "cv";
    }

    @GetMapping("/deleteExperience")
    public String deleteExperience(Model model, int i) {
        this.experienceList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "cv";
    }

    @GetMapping("/deleteFormation")
    public String deleteFormation(Model model, int i) {
        this.formationList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "cv";
    }


    @GetMapping("/deleteLangue")
    public String deleteLangue(Model model, int i) {
        this.langueList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("contactList", this.contactList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("experienceList", this.experienceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "cv";
    }


    @GetMapping("/edit")
    public String editCv(Principal principal, Model model) {

        Talent talent = userService.findTalentByUsername(principal.getName());

        this.cv = talent.getCv();

        this.contact = new Contact();
        this.oldContactList = new ArrayList<>(this.cv.getContacts());
        this.contactList = new ArrayList<>();

        this.competence = new Competence();
        this.oldCompetenceList = new ArrayList<>(this.cv.getCompetences());
        this.competenceList = new ArrayList<>();

        this.formation = new Formation();
        this.oldFormationList = new ArrayList<>(this.cv.getFormations());
        this.formationList = new ArrayList<>();

        this.experience = new Experience();
        this.oldExperienceList = new ArrayList<>(this.cv.getExperiences());
        this.experienceList = new ArrayList<>();

        this.langue = new Langue();
        this.oldLangueList = new ArrayList<>(this.cv.getLangues());
        this.langueList = new ArrayList<>();


        model.addAttribute("cv", this.cv);

        model.addAttribute("contact", this.contact);
        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        model.addAttribute("formation", this.formation);
        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("experience", this.experience);
        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("langue", this.langue);
        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        return "editCv";

    }


    @PostMapping("/newContact")
    public String newContact(@ModelAttribute Contact contact, @ModelAttribute Competence competence, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.contactList.add(contact);
        this.contact = new Contact();
        model.addAttribute("cv", this.cv);
        model.addAttribute("contact", this.contact);
        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);


        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);


        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);


        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);


        return "editCv";
    }

    @PostMapping("/newCompetence")
    public String newCompetence(@ModelAttribute Contact contact, @ModelAttribute Competence competence, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.competenceList.add(competence);
        this.competence = new Competence();
        model.addAttribute("cv", this.cv);
        model.addAttribute("competence", this.competence);
        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);


        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);


        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        return "editCv";
    }

    @PostMapping("/newFormation")
    public String newFormation(@ModelAttribute Contact contact, @ModelAttribute Competence competence, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.formationList.add(formation);
        this.formation = new Formation();
        model.addAttribute("cv", this.cv);
        model.addAttribute("formation", this.formation);
        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);


        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);


        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);


        return "editCv";
    }


    @PostMapping("/newExperience")
    public String newExperience(@ModelAttribute Contact contact, @ModelAttribute Competence competence, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.experienceList.add(experience);
        this.experience = new Experience();
        model.addAttribute("cv", this.cv);
        model.addAttribute("experience", this.experience);
        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        return "editCv";
    }

    @PostMapping("/newLangue")
    public String newLangue(@ModelAttribute Contact contact, @ModelAttribute Competence competence, @ModelAttribute Formation formation, @ModelAttribute Experience experience, @ModelAttribute Langue langue, Model model) {
        this.langueList.add(langue);
        this.langue = new Langue();
        model.addAttribute("cv", this.cv);
        model.addAttribute("langue", this.langue);
        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }

    @GetMapping("/deleteNewContact")
    public String deleteNewContact(Model model, int i) {

        this.contactList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);


        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "editCv";
    }

    @GetMapping("/deleteNewCompetence")
    public String deleteNewCompetence(Model model, int i) {

        this.competenceList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "editCv";
    }

    @GetMapping("/deleteNewExperience")
    public String deleteNewExperience(Model model, int i) {

        this.experienceList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "editCv";
    }

    @GetMapping("/deleteNewFormation")
    public String deleteNewFormation(Model model, int i) {

        this.formationList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "editCv";
    }


    @GetMapping("/deleteNewLangue")
    public String deleteNewLangue(Model model, int i) {

        this.langueList.remove(i);

        model.addAttribute("cv", this.cv);

        model.addAttribute("langueList", this.langueList);
        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("experienceList", this.experienceList);
        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("formationList", this.formationList);
        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("contactList", this.contactList);
        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("competenceList", this.competenceList);
        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        this.contact = new Contact();

        this.competence = new Competence();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);


        return "editCv";
    }

    @GetMapping("/deleteOldCompetence")
    public String deleteOldCompetence(Model model, Long id) {

        Cv cv1 = new Cv();
        cv1 = cvService.findCvById(this.cv.getId());
        cv1.getCompetences().remove(competenceService.findCompetenceById(id));
        competenceService.deleteCompetence(id);
        cvService.addCv(cv1);

        this.cv = cv1;

        model.addAttribute("cv", this.cv);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        this.competence = new Competence();

        this.contact = new Contact();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);

        this.oldCompetenceList = new ArrayList<>(this.cv.getCompetences());

        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }

    @GetMapping("/deleteOldContact")
    public String deleteOldContact(Model model, Long id) {

        Cv cv1 = new Cv();
        cv1 = cvService.findCvById(this.cv.getId());
        cv1.getContacts().remove(contactService.findContactById(id));
        contactService.deleteContact(id);
        cvService.addCv(cv1);

        this.cv = cv1;

        model.addAttribute("cv", this.cv);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        this.competence = new Competence();

        this.contact = new Contact();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);

        this.oldContactList = new ArrayList<>(this.cv.getContacts());

        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }

    @GetMapping("/deleteOldExperience")
    public String deleteOldExperience(Model model, Long id) {

        Cv cv1 = new Cv();
        cv1 = cvService.findCvById(this.cv.getId());
        cv1.getExperiences().remove(experienceService.findExperienceById(id));
        experienceService.deleteExperience(id);
        cvService.addCv(cv1);

        this.cv = cv1;

        model.addAttribute("cv", this.cv);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        this.competence = new Competence();

        this.contact = new Contact();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);

        this.oldExperienceList = new ArrayList<>(this.cv.getExperiences());

        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }

    @GetMapping("/deleteOldFormation")
    public String deleteOldFormation(Model model, Long id) {

        Cv cv1 = new Cv();
        cv1 = cvService.findCvById(this.cv.getId());
        cv1.getFormations().remove(formationService.findFormationById(id));
        formationService.deleteFormation(id);
        cvService.addCv(cv1);

        this.cv = cv1;

        model.addAttribute("cv", this.cv);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        this.competence = new Competence();

        this.contact = new Contact();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);

        this.oldFormationList = new ArrayList<>(this.cv.getFormations());

        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }

    @GetMapping("/deleteOldLangue")
    public String deleteOldLangue(Model model, Long id) {

        Cv cv1 = new Cv();
        cv1 = cvService.findCvById(this.cv.getId());
        cv1.getLangues().remove(langueService.findLangueById(id));
        langueService.deleteLangue(id);
        cvService.addCv(cv1);

        this.cv = cv1;

        model.addAttribute("cv", this.cv);

        model.addAttribute("competenceList", this.competenceList);

        model.addAttribute("langueList", this.langueList);

        model.addAttribute("experienceList", this.experienceList);

        model.addAttribute("formationList", this.formationList);

        model.addAttribute("contactList", this.contactList);

        this.competence = new Competence();

        this.contact = new Contact();

        this.formation = new Formation();

        this.experience = new Experience();

        this.langue = new Langue();

        model.addAttribute("contact", this.contact);

        model.addAttribute("competence", this.competence);

        model.addAttribute("formation", this.formation);

        model.addAttribute("experience", this.experience);

        model.addAttribute("langue", this.langue);

        this.oldLangueList = new ArrayList<>(this.cv.getLangues());

        model.addAttribute("oldLangueList", this.oldLangueList);

        model.addAttribute("oldExperienceList", this.oldExperienceList);

        model.addAttribute("oldFormationList", this.oldFormationList);

        model.addAttribute("oldContactList", this.oldContactList);

        model.addAttribute("oldCompetenceList", this.oldCompetenceList);

        return "editCv";
    }


    @GetMapping("/deleteCvPicture")
    public String deleteCvPicture(Long id) {

        Cv cv1 = cvService.findCvById(id);
        cv1.setCvPicture(null);
        cvRepository.save(cv1);

        Long cvId = cv1.getId();
        return "redirect:/cv/edit?id=" + cvId;
    }


    @GetMapping("/myCvInfo")
    public String myCvInfo(Principal principal, Model model) {

        Cv cv = userService.findTalentByUsername(principal.getName()).getCv();

        model.addAttribute("cv", cv);

        return "cvInfo";
    }

    @GetMapping("/cvInfo")
    public String myCvInfo(Model model, Long id) {

        Cv cv = userService.findTalentById(id).getCv();

        model.addAttribute("cv", cv);

        return "cvInfo";
    }

    @GetMapping("/download")
    public void downloadCV(HttpServletResponse response, Long id) throws IOException, DocumentException {

        Context context = new Context();


        Cv cv = cvService.findCvById(id);


        String imagePath = imageDir + cv.getCvPicture();
        String base64Image = encodeImageToBase64(imagePath);


        context.setVariable("cvPictureBase64", base64Image);
        context.setVariable("cv", cv);


        String htmlContent = templateEngine.process("cvInformations", context);


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();


        String baseUrl = "file://" + new ClassPathResource("static/").getURL().getPath();
        renderer.setDocumentFromString(htmlContent, baseUrl);

        renderer.setPDFVersion(PdfWriter.VERSION_1_7);
        renderer.layout();


        renderer.createPDF(outputStream, true);


        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + cv.getTalent().getPrenom() + " " + cv.getTalent().getNom() + "CV.pdf");

        response.getOutputStream().write(outputStream.toByteArray());
    }

    private String encodeImageToBase64(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        byte[] imageBytes = FileUtils.readFileToByteArray(imageFile);
        return Base64.getEncoder().encodeToString(imageBytes);
    }


    @RequestMapping(value = "/getCvPicture", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getCvPicture(String nomFile) throws Exception {
        File f = new File(imageDir + nomFile);
        return IOUtils.toByteArray(new FileInputStream(f));
    }


}