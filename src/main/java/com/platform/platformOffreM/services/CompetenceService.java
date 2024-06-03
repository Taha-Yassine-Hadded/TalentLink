package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.CvInfos.Competence;

import java.util.List;

public interface CompetenceService {

    Competence addCompetence(Competence competence);

    String updateCompetence(Long id, Competence competence);

    String deleteCompetence(Long id);

    List<Competence> findAllCompetence();

    Competence findCompetenceById(Long id);








}
