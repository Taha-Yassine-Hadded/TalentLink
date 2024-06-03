package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.Competence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenceRepository extends JpaRepository<Competence, Long> {

    List<Competence> findByCv(Cv cv);
}
