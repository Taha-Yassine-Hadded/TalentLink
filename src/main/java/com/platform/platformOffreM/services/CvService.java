package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Cv;

import java.util.List;

public interface CvService {
    Cv addCv(Cv cv);

    String updateCv(Long id, Cv cv);

    String deleteCv(Long id);

    List<Cv> findAllCv();

    Cv findCvById(Long id);

    Cv findCvByTalentId(Long id);







}
