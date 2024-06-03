package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Follow;
import com.platform.platformOffreM.entities.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long> {

    Follow findByTalentAndEntreprise(Talent talent, Entreprise entreprise);

    int countByEntreprise(Entreprise entreprise);

    List<Follow> findByEntreprise(Entreprise entreprise);



}
