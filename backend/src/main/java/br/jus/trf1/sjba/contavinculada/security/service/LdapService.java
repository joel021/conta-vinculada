package br.jus.trf1.sjba.contavinculada.security.service;

import br.jus.trf1.sjba.contavinculada.exception.AuthenticationException;
import com.imperva.ddc.core.query.ConnectionResponse;
import com.imperva.ddc.core.query.Endpoint;
import com.imperva.ddc.service.DirectoryConnectorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LdapService {

    @Value("${ldap.host}")
    private String lDapHost;

    @Value("${ldap.secundary.host}")
    private String ldapSecundayHost;

    @Value("${ldap.port}")
    private Integer port;

    public boolean authenticate(String userName, String ldapPassword) throws AuthenticationException {

        Endpoint endpoint = new Endpoint();
        endpoint.setSecuredConnection(false);
        endpoint.setSecuredConnectionSecondary(false);
        endpoint.setPort(port);
        endpoint.setSecondaryPort(389);
        endpoint.setHost(lDapHost);
        endpoint.setSecondaryHost(ldapSecundayHost);
        endpoint.setPassword(ldapPassword);

        try {
            endpoint.setUserAccountName(userName);
            ConnectionResponse connectionResponse = DirectoryConnectorService.authenticate(endpoint);
            boolean succeeded = !connectionResponse.isError();
            endpoint.close();
            return succeeded;
        } catch (Exception e) {
            throw new AuthenticationException("Seu usuário ou senha da Justiça estão incorretos. O usuário deve ser a sua matrícula.");
        }

    }

}