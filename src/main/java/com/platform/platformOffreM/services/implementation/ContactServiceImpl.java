package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.CvInfos.Contact;
import com.platform.platformOffreM.repositories.ContactRepository;
import com.platform.platformOffreM.services.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {


        private final ContactRepository contactRepository;


        @Override
        public Contact addContact(Contact contact) {
            return contactRepository.save(contact);
        }

        @Override
        public String updateContact(Long id, Contact contact) {
            if (findContactById(id) != null) {
                if (contact.getType() != null)
                    findContactById(id).setType(contact.getType());
                if (contact.getValue() != null)
                    findContactById(id).setValue(contact.getValue());
                return "Updated";
            }
            return "can't find Contact";
        }

        @Override
        public String deleteContact(Long id) {
            if (findContactById(id) != null) {
                contactRepository.deleteById(id);
                return "Deleted";
            }
            return "can't find Contact";
        }

        @Override
        public List<Contact> findAllContact() {
            return contactRepository.findAll();
        }

        @Override
        public Contact findContactById(Long id) {
            return contactRepository.findById(id).orElse(null);
        }

}
