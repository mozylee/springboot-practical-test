package practice.practicaltest.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    public boolean sendMail(String fromEmail, String toEmail, String subject, String text) {
        return true;
    }

}
