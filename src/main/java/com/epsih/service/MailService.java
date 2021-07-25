package com.epsih.service;

import com.epsih.model.mail.NotificationEmail;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Service
public class MailService {

   private final JavaMailSender mailSender;

   public void sendMail(NotificationEmail mail) {
      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setTo(mail.getRecipient(), mail.getRecipient());
      msg.setSubject(mail.getSubject());
      msg.setText(mail.getBody());

      mailSender.send(msg);
   }

   public void sendMailWithAttachments(NotificationEmail mail) throws MessagingException {
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true);

//      helper.setTo("to_@email");
//      helper.setSubject("Testing from Spring Boot");
//      helper.setText("Find the attached image", true);
//      helper.addAttachment("hero.jpg", new ClassPathResource("hero.jpg"));

      mailSender.send(msg);
   }
}
