package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<Cv,Long> {
   Cv findByTalent_Id(Long talentId);
}
