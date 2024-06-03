package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByCv(Cv cv);
}
