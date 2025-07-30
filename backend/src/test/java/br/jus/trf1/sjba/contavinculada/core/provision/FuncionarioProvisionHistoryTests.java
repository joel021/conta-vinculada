package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FuncionarioProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FuncionarioProvisionHistoryTests {

    private static FuncionarioProvisionHistory funcionarioProvisionHistory;
    private static LocalDate dateContract;
    private static LocalDate dateAditive;
    private static LocalDate dateAditiveIncGrupA;
    private static ContratoTerceirizado contratoTerceirizado;
    private static ContratoTerceirizado contratoTerceirizadoAditivo;
    @BeforeAll
    public static void Listup() throws NotAcceptableException {

        dateContract = LocalDate.of(2024,1, 1);
        dateAditive = LocalDate.of(2024, 2, 1);

        Calendar dateContractCalendar = Calendar.getInstance();
        dateContractCalendar.set(2024,1, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContract)
                .criadoEm(dateContractCalendar)
                .remuneracao(new BigDecimal("10.000"))
                .cargo("Cargo 0")
                .build();

        Calendar criadoEm = Calendar.getInstance();
        criadoEm.set(dateAditive.getYear(), dateAditive.getMonth().getValue()-1, dateAditive.getDayOfMonth());
        criadoEm.add(Calendar.MONTH, 2);
        contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateAditive)
                .criadoEm(criadoEm) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(new BigDecimal("11.000"))
                .cargo("Cargo 1")
                .build();

        List<IncGrupoAContrato> incGrupoAContratoes = new ArrayList<>();
        dateAditiveIncGrupA = dateAditive.plusMonths(1);

        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("36.5")).data(dateAditiveIncGrupA).build()); //ordered by date desc
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("34.5")).data(dateContract).build());

        funcionarioProvisionHistory = new FuncionarioProvisionHistory(incGrupoAContratoes);
    }

    @Test
    public void getProvisaoCalcBeforeAditiveTest() {

        ProvisionCalc provisionCalc = funcionarioProvisionHistory.getProvisaoCalc(dateContract);
        BigDecimal expectdPerIncGrupoA = new BigDecimal("34.5");
        assertTrue(expectdPerIncGrupoA.compareTo(provisionCalc.getPER_INC_GRUPO_A()) == 0);
    }

    @Test
    public void getProvisaoCalcAfterAditiveTest() {

        ProvisionCalc provisionCalc = funcionarioProvisionHistory.getProvisaoCalc(dateAditiveIncGrupA);
        BigDecimal expectdPerIncGrupoA = new BigDecimal("36.5");
        assertTrue(expectdPerIncGrupoA.compareTo(provisionCalc.getPER_INC_GRUPO_A()) == 0);
    }

    @Test
    public void getFuncionarioProvisionDateBeforeAditiveTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(dateContract, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(1, provisionList.size());
    }

    @Test
    public void getFuncionarioProvisionDateEqualsAditiveTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(dateAditive, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(2, provisionList.size());
    }

    @Test
    public void getFuncionarioProvisionDateEqualsAditiveIsMostRecentTest() {

        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(dateAditive, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(contratoTerceirizadoAditivo.getCargo(), ((Provision) provisionList.toArray()[0]).getCargo());
    }

    @Test
    public void getFuncionarioProvisionDateAfterAditiveButEqualsToDateOfDiscoveryTest() {

        LocalDate date = fromCalendar(contratoTerceirizadoAditivo.getCriadoEm());
        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(date, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(2, provisionList.size());
    }

    @Test
    public void getFuncionarioProvisionDateAfterAditiveTest() {

        LocalDate date = dateAditive.plusMonths(3);

        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(date, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(2, provisionList.size());
    }

    @Test
    public void getFuncionarioProvisionTwoLateAddictiveTest() {

        LocalDate date = LocalDate.of(2024, Calendar.APRIL, 15);

        contratoTerceirizadoAditivo.setCriadoEm(fromLocalDate(date)); //the contract changed in 2024-FEBRUARY-1 but only discovered after


        ContratoTerceirizado contratoTerceirizadoAditive3 = ContratoTerceirizado.builder()
                .contrato(contratoTerceirizado.getContrato())
                .funcionario(contratoTerceirizado.getFuncionario())
                .remuneracao(new BigDecimal("15.000"))
                .criadoEm(fromLocalDate(date))//discovered in 2024-APRIL-15
                .dataInicio(LocalDate.of(2024, Calendar.MARCH, 12))
                .build();

        List<ContratoTerceirizado> contratoTerceirizadoList = List.of(contratoTerceirizadoAditive3, contratoTerceirizadoAditivo, contratoTerceirizado);
        FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(date, contratoTerceirizadoList);
        List<Provision> provisionList = funcionarioProvision.getProvisoes();
        assertEquals(3, provisionList.size());
    }
}
