package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FuncionarioProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromLocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioProvisionMapperTests {

    private static FuncionarioProvisionMapper funcionarioProvisionMapper;
    private static LocalDate dateContract;
    private static LocalDate dateAditive;
    private static LocalDate dateAditiveIncGrupA;
    private static ContratoTerceirizado contratoTerceirizado;
    private static ContratoTerceirizado contratoTerceirizadoAditivo;
    @BeforeAll
    public static void setup() throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizadoList = new ArrayList<>();

        dateContract = LocalDate.of(2024, 1, 1);
        dateAditive = LocalDate.of(2024, 2, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContract)
                .criadoEm(fromLocalDate(dateContract))
                .remuneracao(10_000)
                .cargo("Cargo 0")
                .build();

        contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateAditive)
                .criadoEm(fromLocalDate(dateAditive.plusMonths(2))) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(11_000)
                .cargo("Cargo 1")
                .build();

        contratoTerceirizadoList.add(contratoTerceirizadoAditivo);//ordered by date desc
        contratoTerceirizadoList.add(contratoTerceirizado);

        List<IncGrupoAContrato> incGrupoAContratoes = new ArrayList<>();
        dateAditiveIncGrupA = LocalDate.of(2024, 1, 1).plusMonths(4);

        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(36.5).data(dateAditiveIncGrupA).build()); //ordered by date desc
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(34.5).data(dateContract).build());

        funcionarioProvisionMapper = new FuncionarioProvisionMapper(contratoTerceirizadoList, incGrupoAContratoes);

    }

    @Test
    public void toSpecificDateFilledTest() {

        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(dateContract);
        assertFalse(provisionList.isEmpty());
    }

    @Test
    public void toSpecificDateHasOneProvisionTest() {

        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(dateContract);
        FuncionarioProvision firstProvision = provisionList.get(0);
        assertEquals(1, firstProvision.getProvisoes().size());
    }

    @Test
    public void toSpecificDateHasTwoProvisionTest() {

        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(dateAditive);
        FuncionarioProvision firstProvision = provisionList.get(0);
        assertEquals(2, firstProvision.getProvisoes().size());
    }

    @Test
    public void toSpecificDateFirstProvisionTest() {

        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(dateContract);
        FuncionarioProvision firstProvision = provisionList.get(0);

        double expectedTotalProvisionadoMensal = new ProvisionCalc(34.5)
                .fromContratoTerceirizado(contratoTerceirizado, dateContract)
                .getTotalProvisaoMensal();

        assertEquals(expectedTotalProvisionadoMensal, ((Provision) firstProvision.getProvisoes().toArray()[0]).getTotalProvisaoMensal());
    }

    @Test
    public void toSpecificDateAfterAdditiveTest() {

        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(dateAditive);
        FuncionarioProvision firstProvision = provisionList.get(0);

        double expectedTotalProvisionadoMensal = new ProvisionCalc(34.5)
                .fromContratoTerceirizado(contratoTerceirizadoAditivo,  dateAditive)
                .getTotalProvisaoMensal();
        expectedTotalProvisionadoMensal = Math.floor(expectedTotalProvisionadoMensal * 100)/100;

        double currentTotalProvisionadoMensal = ((Provision) firstProvision.getProvisoes().toArray()[0]).getTotalProvisaoMensal();
        assertEquals(expectedTotalProvisionadoMensal, Math.floor(currentTotalProvisionadoMensal * 100)/100);
    }

    @Test
    public void toSpecificDateAfterAdditiveIncGrupoATest() {

        LocalDate period = dateAditiveIncGrupA;
        List<FuncionarioProvision> provisionList = funcionarioProvisionMapper.allFuncionarioToSpecificPeriod(period);
        FuncionarioProvision firstProvision = provisionList.get(0);

        double expectedTotalProvisionadoMensal = new ProvisionCalc(36.5)
                .fromContratoTerceirizado(contratoTerceirizadoAditivo, period)
                .getTotalProvisaoMensal();

        assertEquals(expectedTotalProvisionadoMensal, ((Provision) firstProvision.getProvisoes().toArray()[0]).getTotalProvisaoMensal());
    }

    @Test
    public void instantiateProvisionHandlerNullTest() {

        assertThrows(NotAcceptableException.class, () -> {
            new FuncionarioProvisionMapper(new ArrayList<>(), new ArrayList<>());
        });
    }

}
