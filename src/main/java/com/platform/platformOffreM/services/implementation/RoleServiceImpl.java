package com.platform.platformOffreM.services.implementation;

import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.repositories.RoleRepository;
import com.platform.platformOffreM.repositories.UserRepository;
import com.platform.platformOffreM.services.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;


    @Override
    public String addRole(Role role) {

        Set<User> users = role.getUsers();
        users.forEach(user -> user.setRole(role));

        if (findRoleByRoleName(role.getRoleName()) == null) {
            roleRepository.save(role);
            return "role Created";
        }
        return "Role Already Exist";
    }

    @Override
    public String addUserToRole(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRoleName(roleName);
        user.setRole(role);
        userRepository.save(user);
        return "added";
    }

    @Override
    public String updateRoleById(Long id, Role role) {
        if (findRoleById(id) != null) {
            if (role.getRoleName() != null)
                findRoleById(id).setRoleName(role.getRoleName());
            return "updated";
        }
        return "cant find role";
    }

    @Override
    public String deleteRoleById(Long id) {
        if (findRoleById(id) != null) {
            roleRepository.deleteById(id);
            return "deleted";
        }
        return "cant find role";
    }

    @Override
    public String deleteRoleByRoleName(String roleName) {
        if (findRoleByRoleName(roleName) != null) {
            roleRepository.delete(findRoleByRoleName(roleName));
            return "deleted";
        }
        return "cant find role";
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
