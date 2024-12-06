package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromLocalDate;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FuncionarioContratoMapperTests {

    private static FuncionarioContratoMapper funcionarioContratoMapper;

    @BeforeAll
    public static void setup() throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizadoList = new ArrayList<>();

        LocalDate dateContract = LocalDate.of(2024, 1, 1);

        LocalDate dateContractAdictive = LocalDate.of(2024, 2, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContract)
                .criadoEm(fromLocalDate(dateContract))
                .remuneracao(10_000)
                .cargo("Cargo 0")
                .build();

        ContratoTerceirizado contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractAdictive.plusMonths(2))
                .criadoEm(fromLocalDate(dateContractAdictive.plusMonths(2))) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(11_000)
                .cargo("Cargo 1")
                .build();

        contratoTerceirizadoList.add(contratoTerceirizadoAditivo);//ordered by date desc
        contratoTerceirizadoList.add(contratoTerceirizado);

        funcionarioContratoMapper = new FuncionarioContratoMapper(contratoTerceirizadoList);
    }

    @Test
    public void getContratoTerceirizadoListTest() {

        List<ContratoTerceirizado> contratoTerceirizados = funcionarioContratoMapper.getHistory("funcionario1");
        assertFalse(contratoTerceirizados.isEmpty());
    }
}
