package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.services.CvService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/cvs")
public class CvControllerr {

    private final CvService cvService;

    @PostMapping("/add")
    public Cv addCv(@RequestBody Cv cv) {
        return cvService.addCv(cv);
    }

    @PutMapping("/{id}")
    public String updateCv(@PathVariable Long id, @RequestBody Cv cv) {
        return cvService.updateCv(id, cv);
    }

    @DeleteMapping("/{id}")
    public String deleteCv(@PathVariable Long id) {
        return cvService.deleteCv(id);
    }

    @GetMapping("/")
    public List<Cv> getAllCv() {
        return cvService.findAllCv();
    }

    @GetMapping("/{id}")
    public Cv getCvById(@PathVariable Long id) {
        return cvService.findCvById(id);
    }

}
