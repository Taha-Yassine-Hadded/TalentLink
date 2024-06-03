package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Candidature;

import java.util.List;

public interface CandidatureService {

    void deleteCandidatureByTalent_IdAndOffreDeMission_Id(Long talentId, Long offreId);

    Candidature addCondidature(Candidature candidature);

    String updateCondidature(Long id, Candidature candidature);

    String deleteCondidature(Long id);

    List<Candidature> findAllCondidature();

    Candidature findCondidatureById(Long id);

    List<Candidature> findCondidatureByOffreDeMission(Long id);

    List<Candidature> findCondidatureByTalent(Long id);

    Candidature findCandidatureByTalentAndOffre(Long talentId, Long offreId);

    int countCandidatureByOffreDeMission(Long offreId);




}
