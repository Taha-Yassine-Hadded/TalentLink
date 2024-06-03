package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.CvInfos.Formation;
import com.platform.platformOffreM.services.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/formations")
public class FormationController {

    private final FormationService formationService;


    @PostMapping("/add")
    public Formation addFormation(@RequestBody Formation formation) {
        return formationService.addFormation(formation);
    }

    @PutMapping("/{id}")
    public String updateFormation(@PathVariable Long id, @RequestBody Formation formation) {
        return formationService.updateFormation(id, formation);
    }

    @DeleteMapping("/{id}")
    public String deleteFormation(@PathVariable Long id) {
        return formationService.deleteFormation(id);
    }

    @GetMapping("/")
    public List<Formation> getAllFormation() {
        return formationService.findAllFormation();
    }

    @GetMapping("/{id}")
    public Formation findById(@PathVariable Long id) {
        return formationService.findFormationById(id);
    }

}
