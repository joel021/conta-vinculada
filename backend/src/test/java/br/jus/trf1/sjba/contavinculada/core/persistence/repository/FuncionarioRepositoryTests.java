package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.NivelEnsino;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.PessoaFisicaService;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FuncionarioRepositoryTests {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @Autowired
    private UserService userService;

    private PessoaFisica pessoaFisica;

    @BeforeEach
    public void setup() {

        PessoaFisica pessoaFisicaToCreate = new PessoaFisica("00010020039");
        pessoaFisicaToCreate.setNome("Nome Pessoa Física");
        pessoaFisica = pessoaFisicaService.saveIfNotExists(pessoaFisicaToCreate);

        Usuario usuario = new Usuario();
        usuario.setUsuario(UUID.randomUUID().toString());
        usuario.setDominio("JFBA");
        Usuario criadoPor = userService.save(usuario);

        Calendar today = Calendar.getInstance();
        Funcionario funcionario = Funcionario.builder()
                .idFuncionario(1)
                .pessoaFisica(pessoaFisica)
                .matricula("ba00000000000")
                .isOficialJustica(false)
                .nivel(NivelEnsino.I)
                .criadoEm(today)
                .criadoPor(criadoPor)
                .racaCor("Pardo")
                .build();

        funcionarioRepository.save(funcionario);
    }

    @Test
    public void findTest() {
        assertTrue(funcionarioRepository.findById(1).isPresent());
    }

    @Test
    public void findByNomeContainsTest() {

        PessoaFisica pessoaFisicaToSearch = new PessoaFisica();
        pessoaFisicaToSearch.setIdPessoa(pessoaFisica.getIdPessoa());

        List<Funcionario> funcionarioList = funcionarioRepository.findByPessoaFisica(pessoaFisicaToSearch);
        assertFalse(funcionarioList.isEmpty());
    }

    @Test
    public void findNonDeleletedByCpfStartsWithTest() {

        List<Funcionario> funcionarioList = funcionarioRepository.findNonDeleletedByCpfStartsWith("00010");
        assertFalse(funcionarioList.isEmpty());
    }

    @Test
    public void findNonDeleletedByCpfStartsWithNotTest() {

        List<Funcionario> funcionarioList = funcionarioRepository.findNonDeleletedByCpfStartsWith("0039");
        assertTrue(funcionarioList.isEmpty());
    }

}
