package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Admin;
import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.entities.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    String addUser(User user);

    String updateTalentById(Long id, Talent talent);

    String updateEntrepriseById(Long id, Entreprise entreprise);

    String updateAdminById(Long id, Admin admin);

    void deleteUserById(Long id);

    List<User> findAllUsers();

    List<User> findUsersByRole_Id(Long id);

    List<User> findUsersByRole_RoleName(String roleName);

    Talent findTalentById(Long id);

    Talent findTalentByEmail(String email);

    Entreprise findEntrepriseByEmail(String email);

    Admin findAdminByEmail(String email);

    Entreprise findEntrepriseById(Long id);

    Admin findAdminById(Long id);

    User findUserByUsername(String username);

    Talent findTalentByUsername(String username);

    Entreprise findEntrepriseByUsername(String username);

    Admin findAdminByUsername(String username);

    void updateUserResetPasswordToken(String token, String email) throws UserNotFoundException;

    User findUserByResetPasswordToken(String token);

    void updateUserPassword(User user, String newPass);


}
