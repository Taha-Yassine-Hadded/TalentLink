package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.OffreDeMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OffreDeMissionRepository extends JpaRepository<OffreDeMission,Long> {

    List<OffreDeMission> findByEntreprise_Id(Long id);
    int countByEntreprise(Entreprise entreprise);

}
