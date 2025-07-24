package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IncGrupoAContratoRepositoryTests {

    @Autowired
    private IncGrupoAContratoRepository incGrupoAContratoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private UserService userService;

    private Contrato contrato;

    private IncGrupoAContrato incGrupoAContrato;

    private Usuario criador;

    @BeforeEach
    public void setup() {

        LocalDate today = LocalDate.now();
        contrato = new Contrato(1, "SJBA", null,null, today,
                today, today,"893", null);
        contratoRepository.save(contrato);

        criador = new Usuario(null, "criadorGrupoContrato", "Criador Obrigatorio",null,
                null,true,null, "JFBA");
        criador = userService.save(criador);

        incGrupoAContrato = new IncGrupoAContrato(null, LocalDate.now(), contrato, new BigDecimal("93.3333337"),
                criador, Calendar.getInstance(), null, null);
        incGrupoAContrato = incGrupoAContratoRepository.save(incGrupoAContrato);
    }

    @Test
    public void saveTest() {

        IncGrupoAContrato incGrupoAContratoCreated = incGrupoAContratoRepository.save(incGrupoAContrato);
        assertNotNull(incGrupoAContratoCreated.getId());
    }

    @Test
    public void findTest() {
        assertEquals(93.3333337d, incGrupoAContratoRepository.findById(incGrupoAContrato.getId()).get().getIncGrupoA());
    }

    @Test
    public void findByContratoTest() {

        List<IncGrupoAContrato> incGrupoAContratoList = incGrupoAContratoRepository.findByContratoAndDeletadoEm(contrato, null);
        assertFalse(incGrupoAContratoList.isEmpty());
    }

    @Test
    public void findByContratoDeletadoTest() {

        List<IncGrupoAContrato> incGrupoAContratoList = incGrupoAContratoRepository.findByContratoAndDeletadoEm(contrato, Calendar.getInstance());
        assertTrue(incGrupoAContratoList.isEmpty());
    }

    @Test
    public void findByIdCheckCriadorTest() {

        IncGrupoAContrato incGrupoAContratoFound = incGrupoAContratoRepository.findById(incGrupoAContrato.getId()).get();
        assertSame(criador.getUsuario(), incGrupoAContratoFound.getCriadoPor().getUsuario());
    }

}
