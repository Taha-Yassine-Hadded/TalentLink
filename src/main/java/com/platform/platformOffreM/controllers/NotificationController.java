package com.platform.platformOffreM.controllers;


import com.platform.platformOffreM.entities.Notification;
import com.platform.platformOffreM.services.NotificationService;
import com.platform.platformOffreM.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllNotifications(Authentication authentication, Model model) {

        if (authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("TALENT"))) {
                    List<Notification> notifications = notificationService.findNotificationsByTalentId(userService.findTalentByUsername(userDetails.getUsername()).getId());
                    List<Notification> notificationList = new ArrayList<>();
                    for (Notification notif : notifications) {
                        if (notif.getEntreprise() == null) {
                            notificationList.add(notif);
                        }
                    }
                    model.addAttribute("notificationList", notificationList);
                }
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ENTREPRISE"))) {
                    List<Notification> notificationList = notificationService.findNotificationsByEntrepriseId(userService.findEntrepriseByUsername(userDetails.getUsername()).getId());
                    model.addAttribute("notificationList", notificationList);
                }
            }
        }
        return "notifications";
    }


}
