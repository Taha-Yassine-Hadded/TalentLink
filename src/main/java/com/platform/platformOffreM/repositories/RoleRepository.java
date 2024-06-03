package com.platform.platformOffreM.repositories;

import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleName(String roleName);

    List<User> findUsersByRoleName(String roleName);
}
