package com.repositorio.libreria.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sandbox.smtp.mailtrap.io");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        if (list != null && list.size() > 0) {
         message.setCc(getCcArray(list));
         emailSender.send(message);
        }
    }

    private String[] getCcArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // especificar UTF-8
        helper.setFrom("sandbox.smtp.mailtrap.io");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlMsg = "<p><strong>Credenciales de acceso para Librería Crisol</strong></p>"
                + "<p>"
                + "<strong>Correo electrónico:</strong> " + to + "<br>"
                + "<strong>Contraseña:</strong> " + password + "<br><br>"
                + "<a href=\"http://localhost:4200/\" target=\"_blank\" "
                + "style=\"display:inline-block;padding:10px 20px;background-color:#4CAF50;"
                + "color:#ffffff;text-decoration:none;border-radius:6px;font-weight:bold;\">"
                + "Haga clic aquí para iniciar sesión"
                + "</a>"
                + "</p>"
                + "<br><p style=\"font-size:12px;color:gray;\">"
                + "Te recomendamos cambiar tu contraseña después de iniciar sesión por seguridad."
                + "</p>";

        message.setContent(htmlMsg, "text/html; charset=UTF-8");
        emailSender.send(message);
    }

    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // especificar UTF-8
        helper.setFrom("sandbox.smtp.mailtrap.io");
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");
        emailSender.send(message);
    }

}
