package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.CvInfos.Competence;
import com.platform.platformOffreM.entities.OffreDeMission;
import com.platform.platformOffreM.services.OffreDeMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private OffreDeMissionService offreDeMissionService;


    @GetMapping("/test")
    public boolean test() {
        OffreDeMission offreDeMission = new OffreDeMission();
        offreDeMission.setTitre("test");
        Competence competence = new Competence();
        competence.setNom("competence");
        offreDeMission.getCompetences().add(competence);
        offreDeMissionService.addOffre(offreDeMission);
        return true;
    }


    @GetMapping("/test2")
    public OffreDeMission test2() {
        return offreDeMissionService.findOffreById(12L);
    }
}
