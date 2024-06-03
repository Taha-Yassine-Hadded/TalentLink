package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Talent;

import java.util.List;

public interface FollowService {

    void subscribe(Talent talent, Entreprise entreprise);

    void unsubscribe(Talent talent, Entreprise entreprise);

    List<Talent> findTalentsByEntreprise(Entreprise entreprise);
}
