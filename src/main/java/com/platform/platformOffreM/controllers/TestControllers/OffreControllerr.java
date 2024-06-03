package com.platform.platformOffreM.controllers.TestControllers;

import com.platform.platformOffreM.entities.OffreDeMission;
import com.platform.platformOffreM.services.OffreDeMissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offress")
public class OffreControllerr {

    private final OffreDeMissionService offreDeMissionService;


    @PostMapping("/add")
    public OffreDeMission addOffreDeMission(@RequestBody OffreDeMission offreDeMission) {
        return offreDeMissionService.addOffre(offreDeMission);
    }

    @PutMapping("/{id}")
    public String updateOffre(@PathVariable Long id, @RequestBody OffreDeMission offreDeMission) {
        return offreDeMissionService.updateOffre(id, offreDeMission);
    }

    @DeleteMapping("/{id}")
    public String deleteOffre(@PathVariable Long id) {
        return offreDeMissionService.deleteOffre(id);
    }

    @GetMapping("/")
    public List<OffreDeMission> getAllOffre() {
        return offreDeMissionService.findAllOffre();
    }

    @GetMapping("/{id}")
    public OffreDeMission findById(@PathVariable Long id) {
        return offreDeMissionService.findOffreById(id);
    }

    @GetMapping("/entreprise/{id}")
    public List<OffreDeMission> getOffreByEntrepriseID(@PathVariable Long id) {
        return offreDeMissionService.findOffreByEntrepriseID(id);
    }

}
