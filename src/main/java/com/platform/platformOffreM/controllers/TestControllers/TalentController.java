package com.platform.platformOffreM.controllers.TestControllers;

import com.platform.platformOffreM.entities.Talent;
import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.services.RoleService;
import com.platform.platformOffreM.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/talents")
public class TalentController {

    private final UserService userService;
    private final RoleService roleService;


    @PostMapping("/add")
    public String addTalent(@RequestBody Talent talent) {
        talent.setRole(roleService.findRoleByRoleName("TALENT"));
        return userService.addUser(talent);
    }

    @PutMapping("/{id}")
    public String updateTalentById(@PathVariable Long id, @RequestBody Talent talent) {
        return userService.updateTalentById(id, talent);
    }

    @DeleteMapping("/{id}")
    public void deleteTalentById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/")
    public List<User> getAllTalents() {
        return  userService.findUsersByRole_RoleName("TALENT");
    }

    @GetMapping("/{id}")
    public Talent getTalentByID(@PathVariable Long id) {
        return userService.findTalentById(id);
    }

    @GetMapping("/byUsername/{username}")
    public Talent getTalentByUsername(@PathVariable String username) {
        return userService.findTalentByUsername(username);
    }

}
