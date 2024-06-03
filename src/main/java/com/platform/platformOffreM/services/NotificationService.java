package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Notification;
import com.platform.platformOffreM.entities.Talent;

import java.util.List;

public interface NotificationService {

    List<Notification> findNotificationsByTalent(Talent talent);

    List<Notification> findNotificationsByEntreprise(Entreprise entreprise);

    List<Notification> findNotificationsByTalentId(Long talentId);

    List<Notification> findNotificationsByEntrepriseId(Long entrepriseId);



}
