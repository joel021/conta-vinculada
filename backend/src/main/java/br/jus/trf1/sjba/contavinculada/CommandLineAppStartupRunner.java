package br.jus.trf1.sjba.contavinculada;

import br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv.ContratoTerceirizadoSaver;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import br.jus.trf1.sjba.contavinculada.utils.UsersFromJsonFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private ContratoTerceirizadoSaver contratoTerceirizadoSaver;

    private static final Logger LOGGER = Logger.getLogger(CommandLineAppStartupRunner.class.getName());

    @Override
    public void run(String...args) {

        LOGGER.info("Initializing the database.");

        Usuario systemUser = setupSystemUser();
        setupUsers();

        if (!environment.getActiveProfiles()[0].contains("dev")) {
            contratoTerceirizadoSaver.saveContratoTerceirizadoFromResources(systemUser);
        }

        LOGGER.info("Database initialization completed.");
    }

    public void setupUsers() {

        UsersFromJsonFile usersFromJsonFile = new UsersFromJsonFile();
        try {
            for(Usuario usuario: usersFromJsonFile.getUsers(environment.getActiveProfiles()[0])) {
                userService.register(usuario);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public Usuario setupSystemUser() {

        final Usuario systemAdmin = new Usuario(null, "system_user_admin", "Administrador do Sistema",
                null, null, true, List.of(Papel.ROLE_ADMIN), "SJBA");
        return userService.save(systemAdmin);
    }

}