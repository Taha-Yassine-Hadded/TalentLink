package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.CvInfos.Langue;
import com.platform.platformOffreM.services.LangueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/langues")
public class LangueController {

    private final LangueService langueService;


    @PostMapping("/add")
    public Langue addLangue(@RequestBody Langue langue) {
        return langueService.addLangue(langue);
    }

    @PutMapping("/{id}")
    public String updateLangue(@PathVariable Long id, @RequestBody Langue langue) {
        return langueService.updateLangue(id, langue);
    }

    @DeleteMapping("/{id}")
    public String deleteLangue(@PathVariable Long id) {
        return langueService.deleteLangue(id);
    }

    @GetMapping("/")
    public List<Langue> getAllLangue() {
        return langueService.findAllLangue();
    }

    @GetMapping("/{id}")
    public Langue getById(@PathVariable Long id) {
        return langueService.findLangueById(id);
    }

}
