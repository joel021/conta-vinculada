package br.jus.trf1.sjba.contavinculada.core.controller;

import br.jus.trf1.sjba.contavinculada.security.dto.UserAuthenticatedDTO;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    UserAuthenticatedDTO userNonAdmin;

    UserAuthenticatedDTO userAdmin;
}
