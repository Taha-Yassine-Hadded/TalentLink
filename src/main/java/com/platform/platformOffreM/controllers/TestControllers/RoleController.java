package com.platform.platformOffreM.controllers.TestControllers;


import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    private final UserService userService;


    @PostMapping("/add")
    public String addRole(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @PostMapping(value = "/addUserToRole/{username}/{roleName}")
    public String addUserToRole(@PathVariable String username, @PathVariable String roleName) {
        return roleService.addUserToRole(username, roleName);
    }

    @PutMapping("/{id}")
    public String updateRole(@PathVariable Long id, @RequestBody Role role) {
        return roleService.updateRoleById(id, role);
    }

    @DeleteMapping("/ById/{id}")
    public String deleteRoleById(@PathVariable Long id) {
        return roleService.deleteRoleById(id);
    }

    @DeleteMapping("/ByRoleName/{roleName}")
    public String deleteRoleByRoleName(@PathVariable String roleName) {
        return roleService.deleteRoleByRoleName(roleName);
    }

    @GetMapping("/")
    public List<Role> getAllRole() {
        return roleService.getAllRole();
    }

    @GetMapping("/ById/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.findRoleById(id);
    }

    @GetMapping("/ByRoleName/{roleName}")
    public Role getRoleByRoleName(@PathVariable String roleName) {
        return roleService.findRoleByRoleName(roleName);
    }

    @GetMapping("users/ByRoleId/{id}")
    public List<User> getUsersByRoleId(@PathVariable Long id) {
        return userService.findUsersByRole_Id(id);
    }

    @GetMapping("users/ByRoleName/{roleName}")
    public List<User> getUsersByRoleName(@PathVariable String roleName) {
        return userService.findUsersByRole_RoleName(roleName);
    }



}
