package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.CvInfos.Experience;

import java.util.List;

public interface ExperienceService {

    Experience addExperience(Experience experience);

    String updateExperience(Long id, Experience experience);

    String deleteExperience(Long id);

    List<Experience> findAllExperience();

    Experience findExperienceById(Long id);


}
