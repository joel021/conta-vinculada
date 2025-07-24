package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FuncionarioContratosMapperTests {

    private List<ContratoTerceirizado> contratoTerceirizadoList;

    @BeforeEach
    public void setup() {

        contratoTerceirizadoList = new ArrayList<>();

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
        for(int i = 0; i < 10; i++) {

            dataInicio = dataInicio.plusDays(15-i);
            ContratoTerceirizado employeeContract = new ContratoTerceirizado(
                    i,
                    funcionario,
                    contrato,
                    "Some Cargo",
                    new BigDecimal("100.0").add(new BigDecimal(""+i)),
                    40,
                    dataInicio,
                    criadoPor,
                    criadoEm,
                    null,
                    null,
                    null,
                    null
            );

            contratoTerceirizadoList.add(employeeContract);
        }

    }

    @Test
    public void funcionarioContratosDtoListFromEmpty() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(new ArrayList<>());
        Collection<FuncionarioContratosDto> funcionarioContratosDtos = funcionarioContratosMapper.funcionarioContratosDtoList();
        assertEquals(0, funcionarioContratosDtos.size());
    }

    @Test
    public void funcionarioContratosDtoListFromNull() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(null);
        Collection<FuncionarioContratosDto> funcionarioContratosDtos = funcionarioContratosMapper.funcionarioContratosDtoList();
        assertEquals(0, funcionarioContratosDtos.size());
    }

    @Test
    public void funcionarioContratosDtoListSizeTest() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(contratoTerceirizadoList);
        Collection<FuncionarioContratosDto> funcionarioContratosDtos = funcionarioContratosMapper.funcionarioContratosDtoList();
        assertEquals(1, funcionarioContratosDtos.size());
    }

    @Test
    public void funcionarioContratosDtoEmployeeHas10Test() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(contratoTerceirizadoList);
        Collection<FuncionarioContratosDto> funcionarioContratosDtos = funcionarioContratosMapper.funcionarioContratosDtoList();
        assertEquals(10, funcionarioContratosDtos.stream().toList().get(0).getContratoTerceirizadoList().size());
    }

    @Test
    public void getFuncionarioContratosDtoFromMapContratoTerceirizadoListTest() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(contratoTerceirizadoList);

        final var contratosFuncionarioDto = funcionarioContratosMapper.getFuncionarioContratosDtoFromMap(
                contratoTerceirizadoList.get(0)
        );
        assertNotNull(contratosFuncionarioDto.getContratoTerceirizadoList());
    }

    @Test
    public void getFuncionarioContratosDtoFromMapContratoTest() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(contratoTerceirizadoList);
        FuncionarioContratosDto funcionarioContratosDto = funcionarioContratosMapper.getFuncionarioContratosDtoFromMap(
                contratoTerceirizadoList.get(0)
        );
        assertNotNull(funcionarioContratosDto.getContrato());
    }

    @Test
    public void sortEachByDataInicioTest() {

        FuncionarioContratosMapper funcionarioContratosMapper = new FuncionarioContratosMapper(contratoTerceirizadoList);
        final var funcionarioContratosList = funcionarioContratosMapper.funcionarioContratosDtoList();
        assertEquals(9, funcionarioContratosList.stream().toList().get(0).getContratoTerceirizadoList().get(0).getId());

    }
}
