package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.CvInfos.Langue;
import com.platform.platformOffreM.repositories.LangueRepository;
import com.platform.platformOffreM.services.LangueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class LangueServiceImpl implements LangueService {

    private final LangueRepository langueRepository;


    @Override
    public Langue addLangue(Langue langue) {
        return langueRepository.save(langue);
    }

    @Override
    public String updateLangue(Long id, Langue langue) {
        if (findLangueById(id) != null) {
            if (langue.getNom() != null)
                findLangueById(id).setNom(langue.getNom());
            if (langue.getNiveau() != null)
                findLangueById(id).setNiveau(langue.getNiveau());
            if (langue.getCv() != null)
                findLangueById(id).setCv(langue.getCv());
            return "Updated";
        }
        return "can't find Langue";
    }

    @Override
    public String deleteLangue(Long id) {
        if (findLangueById(id) != null) {
            langueRepository.deleteById(id);
            return "Deleted";
        }
        return "can't find Langue";
    }

    @Override
    public List<Langue> findAllLangue() {
        return langueRepository.findAll();
    }

    @Override
    public Langue findLangueById(Long id) {
        return langueRepository.findById(id).orElse(null);
    }

}
