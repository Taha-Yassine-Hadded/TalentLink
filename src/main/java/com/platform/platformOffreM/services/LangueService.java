package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.CvInfos.Langue;

import java.util.List;

public interface LangueService {
    Langue addLangue(Langue langue);

    String updateLangue(Long id, Langue langue);

    String deleteLangue(Long id);

    List<Langue> findAllLangue();

    Langue findLangueById(Long id);

}
