package com.platform.platformOffreM.controllers;


import com.platform.platformOffreM.services.implementation.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
@RequestMapping("/contactUs")
public class ContactUsController {

    private final EmailService emailService;

    @PostMapping
    public String contactUs(HttpServletRequest request) throws MessagingException {
        String nomPrenom = request.getParameter("nomPrenom");
        String from = request.getParameter("from");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        String body = "Nom et Prénom : " + nomPrenom + "\n" + "Email: " + from + "\n" + "Message: " + content;

        emailService.sendEmail("talentlinkk@gmail.com", subject, body);

        return "redirect:/";
    }

}
