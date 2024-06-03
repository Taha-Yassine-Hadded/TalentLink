package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.Candidature;
import com.platform.platformOffreM.services.CandidatureService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;


    @PostMapping("/add")
    public Candidature addCandidature(@RequestBody Candidature candidature) {
        return candidatureService.addCondidature(candidature);
    }

    @PutMapping("/{id}")
    public String updateCandidature(@PathVariable Long id, @RequestBody Candidature candidature) {
        return candidatureService.updateCondidature(id, candidature);
    }

    @DeleteMapping("/{id}")
    public String deleteCandidature(@PathVariable Long id) {
        return candidatureService.deleteCondidature(id);
    }

    @GetMapping("/")
    public List<Candidature> getAllCandidature() {
        return candidatureService.findAllCondidature();
    }

    @GetMapping("/{id}")
    public Candidature getCandidatureById(@PathVariable Long id) {
        return candidatureService.findCondidatureById(id);
    }

    @GetMapping("/ByOffre/{id}")
    public List<Candidature> getCandidatureByOffreDeMission(@PathVariable Long id) {
        return candidatureService.findCondidatureByOffreDeMission(id);
    }

    @GetMapping("/ByTalent/{id}")
    public List<Candidature> getCandidatureByTalent(@PathVariable Long id) {
        return candidatureService.findCondidatureByTalent(id);
    }

}
