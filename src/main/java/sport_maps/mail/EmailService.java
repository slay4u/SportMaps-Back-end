package sport_maps.mail;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendHtmlEmail(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress("sportmaps.official@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject("Account verification");
            String htmlContent = "<p>Please, confirm email verification by clicking this link:</p>" +
                    "<a href=\"" + link + "\">Verify email</a>";
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while sending email.");
        }
    }
}
