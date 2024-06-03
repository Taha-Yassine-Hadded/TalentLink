package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Notification;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.repositories.NotificationRepository;
import com.platform.platformOffreM.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;




    @Override
    public List<Notification> findNotificationsByTalent(Talent talent) {
        return notificationRepository.findByTalentOrderByDateDesc(talent);
    }

    @Override
    public List<Notification> findNotificationsByEntreprise(Entreprise entreprise) {
        return notificationRepository.findByEntrepriseOrderByDateDesc(entreprise);
    }

    @Override
    public List<Notification> findNotificationsByTalentId(Long talentId) {
        return notificationRepository.findByTalent_IdOrderByDateDesc(talentId);
    }

    @Override
    public List<Notification> findNotificationsByEntrepriseId(Long entrepriseId) {
        return notificationRepository.findByEntreprise_IdOrderByDateDesc(entrepriseId);
    }
}
