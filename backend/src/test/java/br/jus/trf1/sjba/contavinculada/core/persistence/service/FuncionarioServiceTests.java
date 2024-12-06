package br.jus.trf1.sjba.contavinculada.core.persistence.service;


import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.FuncionarioRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FuncionarioServiceTests {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    private Funcionario funcionario;

    @BeforeEach
    public void setup() {

        PessoaFisica pessoaFisica = PessoaFisica.builder().cpf("00000000000").build();
        funcionario = Funcionario.builder()
                .idFuncionario(1)
                .matricula("ba00000000000")
                .pessoaFisica(pessoaFisica)
                .build();
        when(funcionarioRepository.findByMatricula(funcionario.getMatricula())).thenReturn(Optional.of(funcionario));
        when(funcionarioRepository.findById(funcionario.getIdFuncionario())).thenReturn(Optional.of(funcionario));
    }

    @Test
    public void saveIfNotExistsTest() throws NotAcceptableException {

        Funcionario funcionarioSaved = funcionarioService.saveIfNotExists(funcionario);
        assertEquals(funcionario.getIdFuncionario(), funcionarioSaved.getIdFuncionario());
    }

    @Test
    public void saveIfNotExistsNullTest() {

        assertThrows(NotAcceptableException.class, () -> {
            Funcionario funcionarioSaved = funcionarioService.saveIfNotExists(null);
        });
    }

    @Test
    public void findByMatriculaOrThrowsTest() throws NotFoundException {

        Funcionario funcionarioFound = funcionarioService.findByMatriculaOrThrows(funcionario.getMatricula());
        assertEquals(funcionario.getIdFuncionario(), funcionarioFound.getIdFuncionario());
    }

    @Test
    public void findByMatriculaOrThrowsNotFoundTest() {

        assertThrows(NotFoundException.class, () -> {
            Funcionario funcionarioFound = funcionarioService.findByMatriculaOrThrows("matriculaNotExists");
        });
    }
}
