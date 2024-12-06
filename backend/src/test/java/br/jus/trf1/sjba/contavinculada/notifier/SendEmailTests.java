package br.jus.trf1.sjba.contavinculada.notifier;

import org.apache.commons.mail.EmailException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;


@SpringBootTest
public class SendEmailTests {

    @MockBean
    private EmailService emailService;

    @Test
    public void sendEmailTests() throws EmailException {

        Set<String> emails = new HashSet<>();
        emails.add("joel.mpires@trf1.jus.br");
        emails.add("lucas.soliveira@trf1.jus.br");
        emailService.sendHTMLEmail(emails, "Test", "Email de teste");
    }


}
