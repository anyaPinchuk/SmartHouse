package beans.services;

import dto.UserDTO;
import exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

    public boolean sendEmail(final UserDTO userDTO, final String confirmationToken) {

        SimpleMailMessage email = getSimpleMailMessage(userDTO, confirmationToken);
        try {
            mailSender.send(email);
            return true;
        } catch (MailException e) {
            LOG.error(e.getMessage());
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private SimpleMailMessage getSimpleMailMessage(final UserDTO accountDTO, final String confirmationToken) {
        String recipientAddress = accountDTO.getEmail();
        String subject = "Confirm email from Smart house";
        String confirmationUrl = environment.getProperty("email.confirm.url") + "?token=" + confirmationToken;
        String message = "";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("SmartHouse");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \n " + environment.getProperty("app.host") + confirmationUrl);
        return email;
    }
}
