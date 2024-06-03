package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.CvInfos.Competence;
import com.platform.platformOffreM.repositories.CompetenceRepository;
import com.platform.platformOffreM.services.CompetenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CompetenceServiceImpl implements CompetenceService {

    private final CompetenceRepository competenceRepository;


    @Override
    public Competence addCompetence(Competence competence) {
        return competenceRepository.save(competence);
    }

    @Override
    public String updateCompetence(Long id, Competence competence) {
        if (findCompetenceById(id) != null) {
            if (competence.getNom() != null)
                findCompetenceById(id).setNom(competence.getNom());
            if (competence.getNiveau() != null)
                findCompetenceById(id).setNiveau(competence.getNiveau());
            if (competence.getCv() != null)
                findCompetenceById(id).setCv(competence.getCv());
            return "Updated";
        }
        return "Can't find competence";
    }

    @Override
    public String deleteCompetence(Long id) {
        if (findCompetenceById(id) != null) {
            competenceRepository.deleteById(id);
            return "Deleted";
        }
        return "Can't find competence";
    }

    @Override
    public List<Competence> findAllCompetence() {
        return competenceRepository.findAll();
    }

    @Override
    public Competence findCompetenceById(Long id) {
        return competenceRepository.findById(id).orElse(null);
    }

}
