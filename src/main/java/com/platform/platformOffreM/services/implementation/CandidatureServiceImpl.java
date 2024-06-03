package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Candidature;
import com.platform.platformOffreM.repositories.CandidatureRepository;
import com.platform.platformOffreM.services.CandidatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class CandidatureServiceImpl implements CandidatureService {

    private final CandidatureRepository candidatureRepository;


    @Override
    public void deleteCandidatureByTalent_IdAndOffreDeMission_Id(Long talentId, Long offreId) {
        candidatureRepository.deleteByTalent_IdAndOffreDeMission_Id(talentId,offreId);
    }

    @Override
    public Candidature addCondidature(Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    @Override
    public String updateCondidature(Long id, Candidature candidature) {
        if (findCondidatureById(id) != null) {
            if (candidature.getDate() != null)
                findCondidatureById(id).setDate(candidature.getDate());
            if (candidature.getStatus() != null)
                findCondidatureById(id).setStatus(candidature.getStatus());
            if (candidature.getTalent() != null)
                findCondidatureById(id).setTalent(candidature.getTalent());
            if (candidature.getOffreDeMission() != null)
                findCondidatureById(id).setOffreDeMission(candidature.getOffreDeMission());
            return "Updated";
        }
        return "can't find Condidature";
    }

    @Override
    public String deleteCondidature(Long id) {
        if (findCondidatureById(id) != null) {
            candidatureRepository.deleteById(id);
            return "Deleted";
        }
        return "Can't find Condidature";
    }

    @Override
    public List<Candidature> findAllCondidature() {
        return candidatureRepository.findAll();
    }

    @Override
    public Candidature findCondidatureById(Long id) {
        return candidatureRepository.findById(id).orElse(null);
    }

    @Override
    public List<Candidature> findCondidatureByOffreDeMission(Long id) {
        return candidatureRepository.findByOffreDeMission_Id(id);
    }

    @Override
    public List<Candidature> findCondidatureByTalent(Long id) {
        return candidatureRepository.findByTalent_Id(id);
    }

    @Override
    public Candidature findCandidatureByTalentAndOffre(Long talentId, Long offreId) {
        return candidatureRepository.findByTalent_IdAndOffreDeMission_Id(talentId,offreId);
    }

    @Override
    public int countCandidatureByOffreDeMission(Long offreId) {
        return candidatureRepository.countByOffreDeMission_Id(offreId);
    }

}
