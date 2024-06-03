package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormationRepository extends JpaRepository<Formation,Long> {

    List<Formation> findByCv(Cv cv);
}
