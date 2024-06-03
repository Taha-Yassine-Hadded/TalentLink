package com.platform.platformOffreM.services;

import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.entities.User;

import java.util.List;

public interface RoleService {

    String addRole(Role role);

    String addUserToRole(String username, String roleName);

    String updateRoleById(Long id, Role role);

    String deleteRoleById(Long id);

    String deleteRoleByRoleName(String roleName);

    List<Role> getAllRole();

    Role findRoleById(Long id);

    Role findRoleByRoleName(String roleName);

}
