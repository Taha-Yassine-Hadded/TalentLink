package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Candidature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    Page<Candidature> findByOffreDeMission_Id(Long id, Pageable pageable);

    void deleteByTalent_IdAndOffreDeMission_Id(Long talentId, Long offreId);

    int countByOffreDeMission_Id(Long offreId);

    List<Candidature> findByOffreDeMission_Id(Long id);

    List<Candidature> findByTalent_Id(Long id);

    Candidature findByTalent_IdAndOffreDeMission_Id(Long talentId, Long offreId);
}
