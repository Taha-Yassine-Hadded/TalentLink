package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Candidature;
import com.platform.platformOffreM.entities.CvInfos.Competence;
import com.platform.platformOffreM.entities.OffreDeMission;
import com.platform.platformOffreM.repositories.OffreDeMissionRepository;
import com.platform.platformOffreM.services.OffreDeMissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;


@Service
@Transactional
@AllArgsConstructor
public class OffreDeMissionServiceImpl implements OffreDeMissionService {

    private final OffreDeMissionRepository offreDeMissionRepository;


    @Override
    public OffreDeMission addOffre(OffreDeMission offreDeMission) {

        Set<Competence> competences = offreDeMission.getCompetences();
        competences.forEach(competence -> competence.setOffreDeMission(offreDeMission));

        Set<Candidature> candidatures = offreDeMission.getCandidatures();
        candidatures.forEach(candidature -> candidature.setOffreDeMission(offreDeMission));

        return offreDeMissionRepository.save(offreDeMission);
    }


    @Override
    public String updateOffre(Long id, OffreDeMission offreDeMission) {
        if (findOffreById(id) != null) {
            if (offreDeMission.getTitre() != null)
                findOffreById(id).setTitre(offreDeMission.getTitre());
            if (offreDeMission.getCandidatures() != null)
                findOffreById(id).setCandidatures(offreDeMission.getCandidatures());
            if (offreDeMission.getCompetences() != null)
                findOffreById(id).setCompetences(offreDeMission.getCompetences());
            if (offreDeMission.getEntreprise() != null)
                findOffreById(id).setEntreprise(offreDeMission.getEntreprise());
            if (offreDeMission.getDescription() != null)
                findOffreById(id).setDescription(offreDeMission.getDescription());
            return "Updated";
        }
        return "can't find Offre";
    }

    @Override
    public String deleteOffre(Long id) {
        if (findOffreById(id) != null) {
            offreDeMissionRepository.deleteById(id);
            return "Deleted";
        }
        return "can't find offre";
    }

    @Override
    public List<OffreDeMission> findAllOffre() {
        return offreDeMissionRepository.findAll();
    }

    @Override
    public OffreDeMission findOffreById(Long id) {
        return offreDeMissionRepository.findById(id).orElse(null);
    }

    @Override
    public List<OffreDeMission> findOffreByEntrepriseID(Long id) {
        return offreDeMissionRepository.findByEntreprise_Id(id);
    }

}
