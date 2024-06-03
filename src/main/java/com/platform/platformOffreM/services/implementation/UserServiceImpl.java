package com.platform.platformOffreM.services.implementation;


import com.platform.platformOffreM.entities.*;
import com.platform.platformOffreM.repositories.UserRepository;
import com.platform.platformOffreM.services.UserNotFoundException;
import com.platform.platformOffreM.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private  final PasswordEncoder passwordEncoder;


    @Override
    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Added";
    }

    @Override
    public String updateTalentById(Long id, Talent talent) {
        if (findTalentById(id) != null) {
            if (talent.getNom() != null)
                findTalentById(id).setNom(talent.getNom());
            if (talent.getGenre() != null)
                findTalentById(id).setGenre(talent.getGenre());
            if (talent.getNom() != null)
                findTalentById(id).setPrenom(talent.getPrenom());
            if (talent.getDateNaissance() != null)
                findTalentById(id).setDateNaissance(talent.getDateNaissance());
            if (talent.getUsername() != null)
                findTalentById(id).setUsername(talent.getUsername());
            if (talent.getEmail() != null)
                findTalentById(id).setEmail(talent.getEmail());
            if (talent.getCv() != null)
                findTalentById(id).setCv(talent.getCv());
            if (talent.getCandidatures() != null)
                findTalentById(id).setCandidatures(talent.getCandidatures());
            if (talent.getProfilePicture() != null)
                findTalentById(id).setProfilePicture(talent.getProfilePicture());
            return "Updated Talent";
        }
        return "can't find Talent";
    }

    @Override
    public String updateEntrepriseById(Long id, Entreprise entreprise) {
        if (findEntrepriseById(id) != null) {
            if (entreprise.getNom() != null)
                findEntrepriseById(id).setNom(entreprise.getNom());
            if (entreprise.getPays() != null)
                findEntrepriseById(id).setPays(entreprise.getPays());
            if (entreprise.getWebsite() != null)
                findEntrepriseById(id).setWebsite(entreprise.getWebsite());
            if (entreprise.getSpecialisation() != null)
                findEntrepriseById(id).setSpecialisation(entreprise.getSpecialisation());
            if (entreprise.getUsername() != null)
                findEntrepriseById(id).setUsername(entreprise.getUsername());
            if (entreprise.getEmail() != null)
                findEntrepriseById(id).setEmail(entreprise.getEmail());
            if (entreprise.getOffreDeMissions() != null)
                findEntrepriseById(id).setOffreDeMissions(entreprise.getOffreDeMissions());
            return "Updated Entreprise";
        }
        return "can't find Entreprise";
    }

    @Override
    public String updateAdminById(Long id, Admin admin) {
        if (findAdminById(id) != null) {
            if (admin.getNom() != null)
                findAdminById(id).setNom(admin.getNom());
            if (admin.getUsername() != null)
                findAdminById(id).setUsername(admin.getUsername());
            if (admin.getEmail() != null)
                findAdminById(id).setEmail(admin.getEmail());
            return "Updated Admin";
        }
        return "can't find Admin";
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findUsersByRole_Id(Long id) {
        return userRepository.findByRole_Id(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public List<User> findUsersByRole_RoleName(String roleName) {
        return userRepository.findByRole_RoleName(roleName);
    }

    @Override
    public Talent findTalentById(Long id) {
        return userRepository.findTalentById(id);
    }

    @Override
    public Talent findTalentByEmail(String email) {
        return userRepository.findTalentByEmail(email);
    }

    @Override
    public Entreprise findEntrepriseByEmail(String email) {
        return userRepository.findEntrepriseByEmail(email);
    }

    @Override
    public Admin findAdminByEmail(String email) {
        return userRepository.findAdminByEmail(email);
    }

    @Override
    public Entreprise findEntrepriseById(Long id) {
        return userRepository.findEntrepriseById(id);
    }

    @Override
    public Admin findAdminById(Long id) {
        return userRepository.findAdminById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Talent findTalentByUsername(String username) {
        return userRepository.findTalentByUsername(username);
    }

    @Override
    public Entreprise findEntrepriseByUsername(String username) {
        return userRepository.findEntrepriseByUsername(username);
    }

    @Override
    public Admin findAdminByUsername(String username) {
        return userRepository.findAdminByUsername(username);
    }

    @Override
    public void updateUserResetPasswordToken(String token, String email) throws UserNotFoundException {
         User user = userRepository.findByEmail(email);

         if (user != null) {
             user.setResetPasswordToken(token);
             userRepository.save(user);
         } else {
             throw new UserNotFoundException("could not find any user with this email : " + email);
         }
    }

    @Override
    public User findUserByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updateUserPassword(User user, String newPass) {
        BCryptPasswordEncoder passwordEncoder1 = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder1.encode(newPass);

        user.setPassword(encodePassword);
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }

}
