package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LiberacaoRepositoryTests {

    @Autowired
    private LiberacaoRepository liberacaoRepository;

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private OficioMovimentacaoRepository oficioMovimentacaoRepository;

    private Usuario criador;

    private ContratoTerceirizado contratoTerceirizado;

    private Liberacao liberacao;

    private final List<Liberacao> liberacaoList = new ArrayList<>();

    @BeforeEach
    public void setup() {

        LocalDate today = LocalDate.now();
        Contrato contrato = new Contrato(1,"SJBA",null,null, today,
                today, today,"893",null);
        contratoRepository.save(contrato);

        Usuario usuario = new Usuario();
        usuario.setUsuario(UUID.randomUUID().toString());
        usuario.setDominio("JFBA");
        criador = userService.save(usuario);

        PessoaFisica pessoaFisica = pessoaFisicaRepository.save(new PessoaFisica("00000000000"));

        Funcionario funcionario = Funcionario.builder().idFuncionario(1).pessoaFisica(pessoaFisica)
                .matricula("ba00000000000").isOficialJustica(false).nivel(NivelEnsino.I).criadoEm(Calendar.getInstance())
                .criadoPor(criador).racaCor("Pardo")
                .build();
        funcionario = funcionarioRepository.save(funcionario);
        contratoTerceirizado = new ContratoTerceirizado(1, funcionario, contrato, "Cargo", 123.45333f,
                40, today, criador, Calendar.getInstance(), null, null, null,
                LocalDate.of(2024,4,1));

        contratoTerceirizadoRepository.save(contratoTerceirizado);

        LocalDate liberacaoDate = LocalDate.of(2022, 1, 1);

        OficioMovimentacao oficioMovimentacao = new OficioMovimentacao(1,1,2023);
        oficioMovimentacaoRepository.save(oficioMovimentacao);

        liberacao = new Liberacao(null, contratoTerceirizado, liberacaoDate, TipoLiberacao.FGTS,
                criador, Calendar.getInstance(), null, null, oficioMovimentacao);
        liberacao = liberacaoRepository.save(liberacao);

        for(int i = 1; i < 10; i++) {
            Liberacao liberacaaoI = Liberacao.builder()
                    .tipo(TipoLiberacao.FGTS)
                    .contratoTerceirizado(contratoTerceirizado)
                    .dataLiberacao(LocalDate.of(2024, i, 1))
                    .oficioMovimentacao(oficioMovimentacao)
                    .criadoPor(criador)
                    .criadoEm(Calendar.getInstance())
                    .build();
            liberacaoList.add(liberacaoRepository.save(liberacaaoI));
        }

    }

    @Test
    public void saveTest() {
        OficioMovimentacao oficioMovimentacao = new OficioMovimentacao(1,1,2023);
        Liberacao liberacao = new Liberacao(null, contratoTerceirizado, LocalDate.now(), TipoLiberacao.FGTS,
                criador, Calendar.getInstance(), null, null, oficioMovimentacao);
        Liberacao liberacaoCreated = liberacaoRepository.save(liberacao);
        assertNotNull(liberacaoCreated);
    }

    @Test
    public void findTest() {

        assertTrue(liberacaoRepository.findById(liberacao.getIdLiberacao()).isPresent());
    }

    @Test
    public void findByContratoAndTipoLiberacaoAndNotDeletedAtTest() {

        List<Liberacao> liberacaoList = liberacaoRepository.findNonDeletedByContratoAndTipoLiberacao(
                contratoTerceirizado.getContrato(),
                TipoLiberacao.FGTS
        );
        assertTrue(liberacaoList.size() >= 5);
    }


}
