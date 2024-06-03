package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.Langue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LangueRepository extends JpaRepository<Langue,Long> {

    List<Langue> findByCv(Cv cv);
}
