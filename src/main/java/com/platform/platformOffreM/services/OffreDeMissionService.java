package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.CvInfos.Competence;
import com.platform.platformOffreM.entities.OffreDeMission;

import java.util.List;

public interface OffreDeMissionService {

    OffreDeMission addOffre(OffreDeMission offreDeMission);
    String updateOffre(Long id, OffreDeMission offreDeMission);

    String deleteOffre(Long id);

    List<OffreDeMission> findAllOffre();

    OffreDeMission findOffreById(Long id);

    List<OffreDeMission> findOffreByEntrepriseID(Long id);


}
