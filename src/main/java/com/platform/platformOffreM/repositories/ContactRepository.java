package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Cv;
import com.platform.platformOffreM.entities.CvInfos.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByCv(Cv cv);
}
