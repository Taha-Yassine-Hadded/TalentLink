package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.CvInfos.Experience;
import com.platform.platformOffreM.repositories.ExperienceRepository;
import com.platform.platformOffreM.services.ExperienceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;


    @Override
    public Experience addExperience(Experience experience) {
        return experienceRepository.save(experience);
    }

    @Override
    public String updateExperience(Long id, Experience experience) {
        if (findExperienceById(id) != null) {
            if (experience.getNomEntreprise() != null)
                findExperienceById(id).setNomEntreprise(experience.getNomEntreprise());
            if (experience.getPoste() != null)
                findExperienceById(id).setPoste(experience.getPoste());
            if (experience.getDate_debut() != null)
                findExperienceById(id).setDate_debut(experience.getDate_debut());
            if (experience.getDate_fin() != null)
                findExperienceById(id).setDate_fin(experience.getDate_fin());
            if (experience.getCv() != null)
                findExperienceById(id).setCv(experience.getCv());
            return "Updated";
        }
        return "can't find Experience";
    }

    @Override
    public String deleteExperience(Long id) {
        if (findExperienceById(id) != null) {
            experienceRepository.deleteById(id);
            return "Deleted";
        }
        return "can't find Experience";
    }

    @Override
    public List<Experience> findAllExperience() {
        return experienceRepository.findAll();
    }

    @Override
    public Experience findExperienceById(Long id) {
        return experienceRepository.findById(id).orElse(null);
    }

}
