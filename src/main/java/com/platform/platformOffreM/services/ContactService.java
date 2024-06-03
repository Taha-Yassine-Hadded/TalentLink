package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.CvInfos.Contact;

import java.util.List;

public interface ContactService {

    Contact addContact(Contact contact);

    String updateContact(Long id, Contact contact);

    String deleteContact(Long id);

    List<Contact> findAllContact();

    Contact findContactById(Long id);

}
