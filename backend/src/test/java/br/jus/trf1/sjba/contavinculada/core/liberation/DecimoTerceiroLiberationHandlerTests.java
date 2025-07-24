package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.provision.DecimoTerceiroPeriodProvision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromLocalDate;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class DecimoTerceiroLiberationHandlerTests {

    private DecimoTerceiroLiberationHandler decimoTerceiroLiberationHandler;
    private Liberacao liberacao;

    @BeforeEach
    public void setup() throws NotAcceptableException {

        LocalDate dateContract = LocalDate.of(2021, 11, 8);

        LocalDate dateContractAdictive = LocalDate.of(2022,2, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContract)
                .criadoEm(fromLocalDate(dateContract))
                .remuneracao(new BigDecimal("6600.0"))
                .cargo("ENGENHEIRO CIVIL carga horária 30h semanais")
                .build();

        LocalDate criadoEm = dateContractAdictive.plusMonths(2);
        //Suppose the employee who controls discovered the change 2 months after
        ContratoTerceirizado contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractAdictive)
                .criadoEm(Calendar.getInstance()) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(new BigDecimal("11.000"))
                .cargo("Cargo 1")
                .build();

        ContratoTerceirizado contratoTerceirizado1 = ContratoTerceirizado.builder()
                .funcionario(Funcionario.builder()
                        .matricula("funcionario12121121")
                        .build())
                .dataInicio(dateContractAdictive)
                .criadoEm(Calendar.getInstance()) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(new BigDecimal("11.000"))
                .cargo("Cargo 2")
                .build();

        LocalDate dateAditiveIncGrupA = LocalDate.of(2022, 1, 1);

        List<IncGrupoAContrato> incGrupoAContratoes = new ArrayList<>();
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("37.8")).data(dateAditiveIncGrupA).build()); //ordered by date desc
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("35.8")).data(dateContract).build());

        liberacao = new Liberacao();
        liberacao.setTipo(TipoLiberacao.DECIMO_TERCEIRO);
        liberacao.setContratoTerceirizado(contratoTerceirizado);

        decimoTerceiroLiberationHandler = new DecimoTerceiroLiberationHandler(
                List.of(contratoTerceirizadoAditivo, contratoTerceirizado, contratoTerceirizado1),
                incGrupoAContratoes
                );
    }

    @Test
    public void addFuncionarioProvisionTest() {

        BigDecimal value = new BigDecimal("814.72");
        BigDecimal multiplier = new BigDecimal("2");
        BigDecimal hundred = new BigDecimal("100");

        LocalDate endYear = LocalDate.of(2021, 12, 31);
        decimoTerceiroLiberationHandler.mapMatriculaLiberations(List.of(liberacao));
        decimoTerceiroLiberationHandler.addFuncionarioProvision("funcionario1", endYear);

        BigDecimal totalProvisionExpected = multiplier.multiply(value)
                .multiply(hundred)
                .setScale(0, RoundingMode.CEILING)
                .divide(hundred, 2, RoundingMode.HALF_EVEN);

        BigDecimal totalProvision = decimoTerceiroLiberationHandler.getFuncionarioPeriodProvisions().get(0)
                .getYearDecimoProvisions().get(2021).getTotalProvision();

        BigDecimal totalProvisionRounded = totalProvision.multiply(hundred)
                .setScale(0, RoundingMode.CEILING)
                .divide(hundred, 2, RoundingMode.HALF_EVEN);

        assertEquals(totalProvisionExpected, totalProvisionRounded);
    }

    @Test
    public void addFuncionarioProvisionFullYearTest() {

        decimoTerceiroLiberationHandler.mapMatriculaLiberations(List.of(liberacao, liberacao));
        decimoTerceiroLiberationHandler.addFuncionarioProvision("funcionario1", LocalDate.of(2022, 12, 31));
        DecimoTerceiroPeriodProvision provision = decimoTerceiroLiberationHandler.getFuncionarioPeriodProvisions().get(0);
        assertEquals(2, provision.getYearDecimoProvisions().size());
    }

    @Test
    @DisplayName("Two professionals. Two liberation for the same professional. Should return two professionals.")
    public void calcTest() {

        List<DecimoTerceiroPeriodProvision> funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao, liberacao),
                LocalDate.of(2022, 12, 31)
        );
        assertEquals(2, funcionarioProvisions.size(), "Should return two professionals.");
    }

    @Test
    @DisplayName("Somehow, the manager registered 3 liberations, the employee has only 2 years of work.")
    public void calcTwoYearsTest() {

        DecimoTerceiroPeriodProvision funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 11, 30)
        ).get(0);
        assertEquals(2, funcionarioProvisions.getYearDecimoProvisions().size());
    }

    @Test
    @DisplayName("The manager registered 3 liberations, the employee has 1 years of work.")
    public void calcOneYearsTest() {

        DecimoTerceiroPeriodProvision funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2021, 12, 30)
        ).get(0);
        assertEquals(1, funcionarioProvisions.getYearDecimoProvisions().size());
    }

    @Test
    @DisplayName("The manager registered 3 liberations, the employee has 3 years of work.")
    public void calcThreeYearsTest() {

        DecimoTerceiroPeriodProvision funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 12, 30)
        ).get(0);
        assertEquals(2, funcionarioProvisions.getYearDecimoProvisions().size());
    }

    @Test
    @DisplayName("The 12 liberation's are of 2022 year.")
    public void calcTwelveLiberationTest() {

        DecimoTerceiroPeriodProvision funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 12, 30)
        ).get(0);
        assertNotNull(funcionarioProvisions.getYearDecimoProvisions().get(2022));
    }

    @Test
    @DisplayName("Period of work in months should be equals to quantity of liberation's when it is greater than 0.")
    public void calcPeriodOfWorkQtdLiberationsTest() {

        DecimoTerceiroPeriodProvision funcionarioProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 12, 30)
        ).get(0);
        assertEquals(14, funcionarioProvisions.getWorkPeriod().workMonths());
    }

    @Test
    @DisplayName("Somehow, exists a liberation of an not existent funcionario yet.")
    public void calcLiberationOfNotFuncionarioNotExistent() {

        Liberacao liberacao = Liberacao.builder()
                .contratoTerceirizado(ContratoTerceirizado.builder()
                        .funcionario(Funcionario.builder()
                                .matricula("funcionarioNotExisten").build())
                        .contrato(Contrato.builder()
                                .numero("numeroContrato")
                                .build())
                        .build())
                .build();
        List<DecimoTerceiroPeriodProvision> funcionarioProvisionsList = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 12, 30)
        );
        Set<String> matriculaSet = funcionarioProvisionsList.stream().map(d -> d.getFuncionario().getMatricula())
                .collect(Collectors.toSet());
        assertFalse(matriculaSet.contains("funcionarioNotExisten"));
    }

    @Test
    @DisplayName("Theres some employees who never had liberation, the system should still calc for them.")
    public void calcForEmployeesWhoNeverHadLiberationTest() {

        List<DecimoTerceiroPeriodProvision> fgtsProvisions = decimoTerceiroLiberationHandler.calc(
                List.of(liberacao),
                LocalDate.now());
        assertEquals(2, fgtsProvisions.size(), "Should include both of 2 employees.");
    }
}
