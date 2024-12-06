package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class AfastamentoRepositoryTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AfastamentoRepository afastamentoRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    private Usuario criador;

    private Contrato contrato;

    private ContratoTerceirizado contratoTerceirizado;

    private Funcionario funcionario;

    private Funcionario funcionarioSubstituto;

    private Afastamento afastamento;

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

        PessoaFisica pessoaFisica00 = pessoaFisicaService.saveIfNotExists(new PessoaFisica("20060000904"));

        funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica00)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I)
                .criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();

        PessoaFisica pessoaFisica01 = pessoaFisicaService.saveIfNotExists(new PessoaFisica("40000100070"));
        funcionarioSubstituto = Funcionario.builder().pessoaFisica(pessoaFisica01)
                .matricula("ba00000100000").isOficialJustica(false).nivel(NivelEnsino.I)
                .criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();
        funcionario = funcionarioRepository.save(funcionario);
        funcionarioSubstituto = funcionarioRepository.save(funcionarioSubstituto);

        contratoTerceirizado = new ContratoTerceirizado(1, funcionario, contrato, "Cargo", 123.45333f,
                40, today, criador, Calendar.getInstance(), null, null, null, null);

        final var contratoTerceirizadoSubstituto = new ContratoTerceirizado(1, funcionario, contrato, "Cargo", 123.45333f,
                40, today, criador, Calendar.getInstance(), null, null, null, null);

        contratoTerceirizadoRepository.save(contratoTerceirizado);
        contratoTerceirizadoRepository.save(contratoTerceirizadoSubstituto);

        LocalDate dataFim = LocalDate.now().plusMonths(60);

        afastamento = Afastamento.builder()
                .substituto(contratoTerceirizadoSubstituto)
                .substituido(contratoTerceirizado)
                .dataInicio(contratoTerceirizado.getDataInicio())
                .dataFim(dataFim)
                .build();

        afastamentoRepository.save(afastamento);
    }

    @Test
    public void findBySubstituidoAndDataInicioTest() {

        var afastamentoList = afastamentoRepository.findBySubstituidoAndDataInicio(
                contratoTerceirizado,
                afastamento.getDataInicio()
        );

        assertFalse(afastamentoList.isEmpty());
    }
}
