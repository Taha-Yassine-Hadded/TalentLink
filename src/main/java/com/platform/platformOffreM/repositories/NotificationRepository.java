package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Notification;
import com.platform.platformOffreM.entities.Talent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByTalentOrderByDateDesc(Talent talent);

    List<Notification> findByEntrepriseOrderByDateDesc(Entreprise entreprise);

    List<Notification> findByTalent_IdOrderByDateDesc(Long talentId);

    List<Notification> findByEntreprise_IdOrderByDateDesc(Long entrepriseId);
}
