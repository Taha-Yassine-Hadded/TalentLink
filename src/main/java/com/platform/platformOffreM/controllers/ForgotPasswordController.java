package com.platform.platformOffreM.controllers;

import com.platform.platformOffreM.entities.User;
import com.platform.platformOffreM.services.UserNotFoundException;
import com.platform.platformOffreM.services.UserService;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm(Model model) {
        return "forgotPasswordForm";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateUserResetPasswordToken(token, email);

            String siteURL = request.getRequestURL().toString();
            String restPasswordLink = siteURL.replace(request.getServletPath(), "") + "/resetPassword?token=" + token;
            sendEmail(email, restPasswordLink);

            model.addAttribute("message", "Nous avons envoyé un lien de réinitialisation du mot de passe à votre adresse e-mail, veuillez vérifier");


        } catch (UserNotFoundException e) {
            model.addAttribute("error", "Impossible de trouver un utilisateur avec cet e-mail");
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Erreur lors de l'envoi de l'e-mail ");
        }


        return "forgotPasswordForm";
    }

    private void sendEmail(String email, String restPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@yasine.com", "TalentLink Support");
        helper.setTo(email);

        String subject = "Voici le lien pour réinitialiser votre mot de passe";

        String content = "<p>Salut,</p>"
                + "<p>Vous avez demandé la réinitialisation de votre mot de passe</p>"
                + "<p>Cliquez sur le lien ci-dessous pour changer votre mot de passe</p>"
                + "<p><b><a href=\"" + restPasswordLink + "\">Changer mon mot de passe</a><b></p>"
                + "<p>Ignorez cet e-mail si vous vous souvenez du mot de passe ou si vous n'avez pas fait la demande pour le changer</p>";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(String token, Model model) {
        User user = userService.findUserByResetPasswordToken(token);
        if (user == null) {
            model.addAttribute("message", "Le token est invalid !");
            return "forgotPasswordForm";
        }
        model.addAttribute("token", token);

        return "resetPasswordForm";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");


        if (confirmPassword.equals(password)) {
            User user = userService.findUserByResetPasswordToken(token);

            userService.updateUserPassword(user, password);
            redirectAttributes.addFlashAttribute("success", "Vous avez changé votre mot de passe avec succès.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Confirmez avec le même mot de passe !");
            return "redirect:/resetPassword?token=" + token;
        }
    }
}
