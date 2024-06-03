package com.platform.platformOffreM.controllers.TestControllers;

import com.platform.platformOffreM.entities.Admin;
import com.platform.platformOffreM.entities.Role;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.repositories.RoleRepository;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admins")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;


    @PostMapping("/add")
    public String addAdmin(@RequestBody Admin admin) {
        admin.setRole(roleService.findRoleByRoleName("ADMIN"));
        return userService.addUser(admin);
    }

    @PutMapping("/{id}")
    public String updateAdminById(@PathVariable Long id, @RequestBody Admin admin) {
        return userService.updateAdminById(id, admin);
    }

    @DeleteMapping("/{id}")
    public void deleteAdminById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/")
    public List<User> getAllAdmins() {
        return userService.findUsersByRole_RoleName("Admin");
    }

    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        return userService.findAdminById(id);
    }

    @GetMapping("/byUsername/{username}")
    public Admin getAdminByUsername(@PathVariable String username) {
        return userService.findAdminByUsername(username);
    }

}
