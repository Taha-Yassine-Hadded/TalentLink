package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.CvInfos.Competence;
import com.platform.platformOffreM.services.CompetenceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/competences")
public class CompetenceControllerr {

    private final CompetenceService competenceService;

    @PostMapping("/add")
    public Competence addComptence(@RequestBody Competence competence) {
        return competenceService.addCompetence(competence);
    }

    @PutMapping("/{id}")
    public String updateCompetence(@PathVariable Long id,@RequestBody Competence competence) {
        return competenceService.updateCompetence(id,competence);
    }

    @DeleteMapping("/{id}")
    public String deleteCompetence(@PathVariable Long id) {
        return competenceService.deleteCompetence(id);
    }

    @GetMapping("/")
    public List<Competence> getAllCompetence() {
        return competenceService.findAllCompetence();
    }

    @GetMapping("/{id}")
    public Competence getCompetenceById(@PathVariable Long id) {
        return competenceService.findCompetenceById(id);
    }

}
