package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoTerceirizadoRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaFisicaRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContratoTerceirizadoServiceTests {

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

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    private Usuario criador;

    private Contrato contrato;

    private Funcionario funcionarioDesligado;

    private Funcionario funcionarioAtivo;

    private ContratoTerceirizado contratoTerceirizado;

    private ContratoTerceirizado contratoTerceirizadoDesligado;

    @BeforeEach
    public void setup() {

        Usuario usuario = new Usuario();
        usuario.setUsuario(UUID.randomUUID().toString());
        usuario.setDominio("JFBA");
        criador = userService.save(usuario);

        LocalDate startContract = LocalDate.of(2021,1,1);
        contrato = new Contrato(1, "SJBA", null, null, startContract,
                startContract, startContract, "893", null);
        contratoRepository.save(contrato);

        PessoaFisica pessoaFisica = pessoaFisicaRepository.save(new PessoaFisica("00000000000"));
        pessoaFisica.setNome("Fulano de tal");

        funcionarioDesligado = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I)
                .criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();

        funcionarioAtivo = Funcionario.builder().idFuncionario(2).pessoaFisica(pessoaFisica)
                .matricula("ba0020030000").isOficialJustica(false).nivel(NivelEnsino.M)
                .criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();

        funcionarioDesligado = funcionarioRepository.save(funcionarioDesligado);
        funcionarioAtivo = funcionarioRepository.save(funcionarioAtivo);

        contratoTerceirizado = new ContratoTerceirizado(1, funcionarioDesligado, contrato, "Cargo", 123.45333f,
                40, startContract, criador, Calendar.getInstance(), null, null, null, null);

        contratoTerceirizadoDesligado = new ContratoTerceirizado(2, funcionarioDesligado, contrato, "Cargo", 123.45333f,
                40, LocalDate.of(2021,1,1), criador, Calendar.getInstance(), null, null, null,
                LocalDate.of(2022,1,1));

        ContratoTerceirizado contratoTerceirizadoAtivo = ContratoTerceirizado
                .builder()
                .id(3)
                .funcionario(funcionarioAtivo)
                .contrato(contrato)
                .cargo("cargo")
                .cargaHoraria(39)
                .criadoPor(criador)
                .criadoEm(Calendar.getInstance())
                .build();

        contratoTerceirizadoRepository.save(contratoTerceirizado);
        contratoTerceirizadoRepository.save(contratoTerceirizadoDesligado);
        contratoTerceirizadoRepository.save(contratoTerceirizadoAtivo);
    }

    @Test
    public void findByFuncionarioAndContratoSortDataInicioDescTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoService
                .findByFuncionarioAndContratoSortDataInicioDesc(funcionarioDesligado.getIdFuncionario(), contrato.getIdContrato());
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findLastByFuncionarioAndContratoTest() throws NotFoundException {

        final var contratoTerceirizadoFound = contratoTerceirizadoService.findLastByFuncionarioAndContrato(
                funcionarioDesligado.getIdFuncionario(),
                contrato.getIdContrato()
        );
        assertNotNull(contratoTerceirizadoFound );
    }

    @Test
    public void saveIfNotExistsTest() throws NotFoundException, NotAcceptableException {

        final var contratoTerceirizadoSaved = contratoTerceirizadoService.saveIfNotExists(contratoTerceirizado, "SJBA");
        assertEquals(contratoTerceirizado.getId(), contratoTerceirizadoSaved.getId());
    }

    @Test
    public void funcionarioContratosDtoListByIdContratoTest() {

        final var funcionarios = contratoTerceirizadoService.funcionarioContratosDtoListByIdContrato(
                contrato.getIdContrato()
        );

        assertFalse(funcionarios.isEmpty());
    }

    @Test
    public void saveIfNotExistsSaveTest() throws NotAcceptableException, NotFoundException {

        ContratoTerceirizado contratoTerceirizadoNotExistent = new ContratoTerceirizado(7, funcionarioDesligado, contrato, "Cargo", 123.45333f,
                40, LocalDate.of(2022,2,11), criador, Calendar.getInstance(), null, null, null, null);

        final var contratoTerceirizadoSaved = contratoTerceirizadoService.saveIfNotExists(contratoTerceirizadoNotExistent, "SJBA");
        assertNotNull(contratoTerceirizadoSaved.getId());
    }

    @Test
    public void findActiveByIdContratoTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoService.findActiveByIdContrato(contrato.getIdContrato());
        assertFalse(contratoTerceirizadoList.isEmpty());
    }

    @Test
    public void findActiveByIdContratoDoesNotHaveInactiveTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoService.findActiveByIdContrato(contrato.getIdContrato());
        Set<String> matriculaSet = contratoTerceirizadoList
                .stream()
                .map(contratoTerceirizado -> contratoTerceirizado.getFuncionario().getMatricula()).collect(Collectors.toSet());

        assertFalse(matriculaSet.contains(funcionarioDesligado.getMatricula()));
    }

    @Test
    public void findActiveByIdContratoHaveActiveTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoService.findActiveByIdContrato(contrato.getIdContrato());
        Set<String> matriculaSet = contratoTerceirizadoList
                .stream()
                .map(contratoTerceirizado -> contratoTerceirizado.getFuncionario().getMatricula()).collect(Collectors.toSet());

        assertTrue(matriculaSet.contains(funcionarioAtivo.getMatricula()));
    }

}
