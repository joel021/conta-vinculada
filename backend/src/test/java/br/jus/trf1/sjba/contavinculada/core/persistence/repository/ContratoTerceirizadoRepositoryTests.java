package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContratoTerceirizadoRepositoryTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    private Usuario criador;

    private Contrato contrato;

    private Funcionario funcionario;

    @BeforeEach
    public void setup() {

        Usuario usuario = new Usuario();
        usuario.setUsuario(UUID.randomUUID().toString());
        usuario.setDominio("JFBA");
        criador = userService.save(usuario);

        LocalDate today = LocalDate.now();
        contrato = new Contrato(1, "SJBA", null, null, today,
                today, today, "893", null);
        contratoRepository.save(contrato);

        PessoaFisica pessoaFisica = pessoaFisicaRepository.save(new PessoaFisica("00000000000"));
        pessoaFisica.setNome("Fulano de tal");

        funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I)
                .criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();

        funcionario = funcionarioRepository.save(funcionario);

        Funcionario funcionarioNotFired = funcionarioRepository.save(
                Funcionario.builder()
                .pessoaFisica(pessoaFisica)
                        .matricula("ba8379540272")
                        .criadoPor(criador)
                        .criadoEm(Calendar.getInstance())
                        .build()
        );

        ContratoTerceirizado contratoTerceirizado = new ContratoTerceirizado(1, funcionario, contrato, "Cargo", 123.45333f,
                40, today, criador, Calendar.getInstance(), null, null, null, null);

        ContratoTerceirizado contratoTerceirizadoDesligado = new ContratoTerceirizado(2, funcionario, contrato, "Cargo", 123.45333f,
                40, LocalDate.of(2021,1,1), criador, Calendar.getInstance(), null, null, null,
                LocalDate.of(2022,1,1));

        ContratoTerceirizado contratoTerceirizadoNotFired = ContratoTerceirizado.builder()
                .contrato(contrato)
                .funcionario(funcionarioNotFired)
                .build();

        contratoTerceirizadoRepository.save(contratoTerceirizado);
        contratoTerceirizadoRepository.save(contratoTerceirizadoDesligado);
        contratoTerceirizadoRepository.save(contratoTerceirizadoNotFired);
    }

    @Test
    public void saveTest() {

        ContratoTerceirizado contratoTerceirizado = new ContratoTerceirizado(null, funcionario, contrato, "Cargo",
                123.45333f, 40, LocalDate.of(2021,2,2), criador, Calendar.getInstance(), null,
                null, null, null);

        ContratoTerceirizado contratoTerceirizadoCreated = contratoTerceirizadoRepository.save(contratoTerceirizado);
        assertNotNull(contratoTerceirizadoCreated.getId());
    }

    @Test
    public void saveAlreadyExistentTest() {

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .contrato(contrato)
                .cargo("Cargo")
                .remuneracao(123.45333f)
                .cargaHoraria(40)
                .dataInicio(LocalDate.now())
                .criadoPor(criador)
                .criadoEm(Calendar.getInstance())
                .deletadoEm(null)
                .build();
        ContratoTerceirizado result = contratoTerceirizadoRepository.save(contratoTerceirizado);
        System.out.println("result: "+result.getId());
    }

    @Test
    public void findTest() {
        float remuneracao = 123.45333f;
        assertEquals(remuneracao, contratoTerceirizadoRepository.findById(1).get().getRemuneracao());
    }

    @Test
    public void findByContratoAndDeletadoEmOrderByDataInicioDescTest() {
        final List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository
                .findByContratoAndDeletadoEmOrderByDataInicioDesc(
                        contrato,
                        null);
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findByFuncionarioAndContratoAndDeletadoEmOrderByDataInicioDescTest() {
        final List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository
                .findByFuncionarioAndContratoAndDeletadoEmOrderByDataInicioDesc(
                        funcionario,
                        contrato,
                        null);
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findByFuncionarioAndContratoAndDataInicioAndDeletadoEmTest() {
        final List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository
                .findByFuncionarioAndContratoAndDataInicioAndDeletadoEm(
                        funcionario,
                        contrato,
                        LocalDate.of(2021,1,1),
                        null);
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findNotDeletedActiveByUnidadeTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository.findNotDeletedActiveByUnidadeOrderByInicioVigenciaDataInicioDesc("SJBA");
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findNotDeletedActiveByUnidadeNotExistentTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository.findNotDeletedActiveByUnidadeOrderByInicioVigenciaDataInicioDesc("SJBA2");
        assertTrue(contratoTerceirizadoList.isEmpty());
    }
}
