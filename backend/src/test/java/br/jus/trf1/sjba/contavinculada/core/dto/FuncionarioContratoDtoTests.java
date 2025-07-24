package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FuncionarioContratoDtoTests {


    private static FuncionarioContratosDto funcionarioContratosDto;

    @BeforeAll
    public static void setup() {

        List<ContratoTerceirizado> contratoTerceirizadoList = new ArrayList<>();
        Funcionario funcionario = new Funcionario();
        funcionario.setIdFuncionario(32);
        funcionario.setMatricula("EMP123");

        Contrato contrato = new Contrato();
        contrato.setIdContrato(1);
        contrato.setUnidade("Some Unit");

        Usuario criadoPor = new Usuario();
        criadoPor.setUsuario("9edi30ei-0dejd");

        LocalDate dataInicio = LocalDate.now();
        Calendar criadoEm = Calendar.getInstance();

        for (int i = 0; i < 10; i++) {

            dataInicio = dataInicio.plusDays(15-i);
            ContratoTerceirizado employeeContract = new ContratoTerceirizado(
                    i,
                    funcionario,
                    contrato,
                    "Some Cargo",
                    new BigDecimal("100.0").add(new BigDecimal(String.valueOf(i))),
                    40,
                    dataInicio,
                    criadoPor,
                    criadoEm,
                    null,
                    null, null, null
            );

            contratoTerceirizadoList.add(employeeContract);
        }

        funcionarioContratosDto = new FuncionarioContratosDto();
        funcionarioContratosDto.setContratoTerceirizadoList(contratoTerceirizadoList);
        assert contratoTerceirizadoList.get(0).getId() == 0;
    }

    @Test
    public void getInstanceFromFuncionarioTest() {

        Funcionario funcionario = new Funcionario();
        FuncionarioContratosDto funcionarioContratosDto = FuncionarioContratosDto.getInstanceFromFuncionario(funcionario);
        assertNotNull(funcionarioContratosDto);
    }

    @Test
    public void sortTest() {

        funcionarioContratosDto.sortDesc();
        assertEquals(9, funcionarioContratosDto.getContratoTerceirizadoList().get(0).getId());
    }
}
