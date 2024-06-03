package com.platform.platformOffreM.controllers.TestControllers;

import com.platform.platformOffreM.entities.CvInfos.Experience;
import com.platform.platformOffreM.services.ExperienceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("experiences")
public class ExperienceController {

    private final ExperienceService experienceService;


    @PostMapping("/add")
    public Experience addExperience(@RequestBody Experience experience) {
        return experienceService.addExperience(experience);
    }

    @PutMapping("/{id}")
    public String updateExperience(@PathVariable Long id, @RequestBody Experience experience) {
        return experienceService.updateExperience(id, experience);
    }

    @DeleteMapping("/{id}")
    public String deleteExperience(@PathVariable Long id) {
        return experienceService.deleteExperience(id);
    }

    @GetMapping("/")
    public List<Experience> showAllExperience() {
        return experienceService.findAllExperience();
    }

    @GetMapping("/{id}")
    public Experience findById(@PathVariable Long id) {
        return experienceService.findExperienceById(id);
    }

}
