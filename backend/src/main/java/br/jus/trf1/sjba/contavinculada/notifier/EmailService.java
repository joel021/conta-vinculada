package br.jus.trf1.sjba.contavinculada.notifier;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class EmailService {

    public static final String MAIL_HOST = "mail.trf1.gov.br";
    public static final String MAIL_FROM = "noreply.seder.ba@trf1.jus.br";

    public void sendHTMLEmail(Set<String> toEmails, String subject, String body) throws EmailException {
        createEmail(toEmails,subject,body).send();
    }

    private HtmlEmail createEmail(Set<String> toEmails,  String subject, String body) throws EmailException {
        HtmlEmail email = new HtmlEmail();

        email.setHostName(MAIL_HOST);

        for (String endereco : toEmails) {
            if (endereco != null && !endereco.isEmpty()){
                email.addTo(endereco);
            }
        }

        email.setSentDate(new Date(System.currentTimeMillis()));
        email.setFrom(MAIL_FROM, "Sistema de Controle de Conta Vinculada - SERFI");
        email.setSubject(subject);
        email.setCharset("utf-8");
        email.setHtmlMsg(body);
        email.setStartTLSEnabled(true);
        email.getMailSession().getProperties().put("mail.smtp.ssl.trust", MAIL_HOST);
        return email;
    }

}
