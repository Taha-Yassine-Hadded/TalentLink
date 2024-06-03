package com.platform.platformOffreM.controllers.TestControllers;

import com.platform.platformOffreM.entities.Entreprise;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/entreprises")
public class EntrepriseController {

    private final UserService userService;
    private final RoleService roleService;


    @PostMapping("/add")
    public String addEntreprise(@RequestBody Entreprise entreprise) {
        entreprise.setRole(roleService.findRoleByRoleName("ENTREPRISE"));
        return userService.addUser(entreprise);
    }

    @PutMapping("/{id}")
    public String updateEntrepriseById(@PathVariable Long id, @RequestBody Entreprise entreprise) {
        return userService.updateEntrepriseById(id, entreprise);
    }

    @DeleteMapping("/{id}")
    public void deleteEntrepriseById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/")
    public List<User> getAllEntreprises() {
        return userService.findUsersByRole_RoleName("entreprise");
    }

    @GetMapping("/{id}")
    public Entreprise getEntrepriseByID(@PathVariable Long id) {
        return userService.findEntrepriseById(id);
    }

    @GetMapping("/byUsername/{username}")
    public Entreprise getEntrepriseByUsername(@PathVariable String username) {
        return userService.findEntrepriseByUsername(username);
    }

}
