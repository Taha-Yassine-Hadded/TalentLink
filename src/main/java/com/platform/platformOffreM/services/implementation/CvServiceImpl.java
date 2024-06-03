package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.*;
import com.platform.platformOffreM.repositories.*;
import com.platform.platformOffreM.services.CvService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;


@Service
@Transactional
@AllArgsConstructor
public class CvServiceImpl implements CvService {

    private final CvRepository cvRepository;


    @Override
    public Cv addCv(Cv cv) {

        Set<Contact> contacts = cv.getContacts();
        contacts.forEach(contact -> contact.setCv(cv));

        Set<Formation> formations = cv.getFormations();
        formations.forEach(formation -> formation.setCv(cv));

        Set<Competence> competences = cv.getCompetences();
        competences.forEach(competence -> competence.setCv(cv));

        Set<Experience> experiences = cv.getExperiences();
        experiences.forEach(experience -> experience.setCv(cv));

        Set<Langue> langues = cv.getLangues();
        langues.forEach(langue -> langue.setCv(cv));

        return cvRepository.save(cv);
    }

    @Override
    public String updateCv(Long id, Cv cv) {
        if (findCvById(id) != null) {
            if (cv.getTitle() != null)
                findCvById(id).setTitle(cv.getTitle());
            if (cv.getIntroduction() != null)
                findCvById(id).setIntroduction(cv.getIntroduction());
            if (cv.getContacts() != null)
                findCvById(id).setContacts(cv.getContacts());
            if (cv.getExperiences() != null)
                findCvById(id).setExperiences(cv.getExperiences());
            if (cv.getCompetences() != null)
                findCvById(id).setCompetences(cv.getCompetences());
            if (cv.getFormations() != null)
                findCvById(id).setFormations(cv.getFormations());
            if (cv.getLangues() != null)
                findCvById(id).setLangues(cv.getLangues());
            return "Updated";
        }
        return "Cant find CV";
    }

    @Override
    public String deleteCv(Long id) {
        if (findCvById(id) != null) {
            cvRepository.deleteById(id);
            return "Deleted";
        }
        return "cant find CV";
    }

    @Override
    public List<Cv> findAllCv() {
        return cvRepository.findAll();
    }

    @Override
    public Cv findCvById(Long id) {
        return cvRepository.findById(id).orElse(null);
    }

    @Override
    public Cv findCvByTalentId(Long id) {
        return cvRepository.findByTalent_Id(id);
    }

}