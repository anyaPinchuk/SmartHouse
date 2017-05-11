package beans.services;

import exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {

    protected Logger LOG = LoggerFactory.getLogger(MailService.class);
    private JavaMailSender mailSender;
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(final String ownerEmail, final String confirmationToken, final Long expireDate) {
        try {
            MimeMessage email = getMimeMessage(ownerEmail, confirmationToken, expireDate);
            mailSender.send(email);
            return true;
        } catch (MailException | MessagingException e) {
            LOG.error(e.getMessage());
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private MimeMessage getMimeMessage(final String ownerEmail, final String confirmationToken, final Long expireDate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ownerEmail);
        String subject = "Confirm email from Smart house";
        String confirmationUrl = environment.getProperty("email.confirm.url") + "?token=" + confirmationToken + "&expire=" + expireDate;
        String msg = "<html><body>To create an account click on link below";
        helper.setFrom("SmartHouse");
        helper.setSubject(subject);
        helper.setText(msg + " \n " + "<a href='" + environment.getProperty("app.host") + confirmationUrl + "'>Here</a></body></html>", true);
        return message;
    }
}
