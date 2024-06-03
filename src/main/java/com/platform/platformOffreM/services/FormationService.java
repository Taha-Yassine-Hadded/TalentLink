package com.platform.platformOffreM.services;


import com.platform.platformOffreM.entities.CvInfos.Formation;

import java.util.List;

public interface FormationService {

    Formation addFormation(Formation formation);

    String updateFormation(Long id, Formation formation);

    String deleteFormation(Long id);

    List<Formation> findAllFormation();

    Formation findFormationById(Long id);







}
