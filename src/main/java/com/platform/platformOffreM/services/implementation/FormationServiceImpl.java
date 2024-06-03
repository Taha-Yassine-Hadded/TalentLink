package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.CvInfos.Formation;
import com.platform.platformOffreM.repositories.FormationRepository;
import com.platform.platformOffreM.services.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class FormationServiceImpl implements FormationService {

    private final FormationRepository formationRepository;


    @Override
    public Formation addFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    @Override
    public String updateFormation(Long id, Formation formation) {
        if (findFormationById(id) != null) {
            if (formation.getEmplacement() != null)
                findFormationById(id).setEmplacement(formation.getEmplacement());
            if (formation.getDiplome() != null)
                findFormationById(id).setDiplome(formation.getDiplome());
            if (formation.getDate_debut() != null)
                findFormationById(id).setDate_debut(formation.getDate_debut());
            if (formation.getDate_fin() != null)
                findFormationById(id).setDate_fin(formation.getDate_fin());
            if (formation.getCv() != null)
                findFormationById(id).setCv(formation.getCv());
            return "Updated";
        }
        return "can't find Formation";
    }

    @Override
    public String deleteFormation(Long id) {
        if (findFormationById(id) != null) {
            formationRepository.deleteById(id);
            return "Deleted";
        }
        return "can't find Formation";
    }

    @Override
    public List<Formation> findAllFormation() {
        return formationRepository.findAll();
    }

    @Override
    public Formation findFormationById(Long id) {
        return formationRepository.findById(id).orElse(null);
    }

}
