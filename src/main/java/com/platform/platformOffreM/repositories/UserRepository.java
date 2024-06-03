package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Admin;
import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    

    boolean existsByEmail(String email);

    boolean existsByNom(String nom);

    boolean existsByUsername(String username);

    Page<User> findByUsernameContainsAndRole_RoleName(String username, String roleName, Pageable pageable);

    Page<User> findByRole_RoleName(String roleName, Pageable pageable);

    User findByUsername(String username);

    Page<User> findByUsername(String username,Pageable pageable);

    Talent findTalentByUsername(String username);

    Entreprise findEntrepriseByUsername(String username);

    Admin findAdminByUsername(String username);

    Talent findTalentById(Long id);

    Talent findTalentByEmail(String email);

    Entreprise findEntrepriseByEmail(String email);

    Admin findAdminByEmail(String email);

    Entreprise findEntrepriseById(Long id);

    Admin findAdminById(Long id);

    List<User> findByRole_Id(Long id);

    List<User> findByRole_RoleName(String roleName);

    User findByResetPasswordToken(String token);

    User findByEmail(String email);

}
