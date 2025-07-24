package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FeriasProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.FeriasPeriodProvision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromLocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class FeriasLiberationHandlerTests {

    private FeriasLiberationHandler feriasLiberationHandler;
    private Liberacao liberacao;

    @BeforeEach
    public void setup() throws NotAcceptableException {

        LocalDate dateContractLocalDate = LocalDate.of(2021, 11, 8);
        LocalDate dateContractAdictiveLocalDate = LocalDate.of(2022, 2, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        ContratoTerceirizado contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractLocalDate)
                .criadoEm(fromLocalDate(dateContractLocalDate))
                .remuneracao(new BigDecimal("6600.0"))
                .cargo("ENGENHEIRO CIVIL carga horária 30h semanais")
                .build();

        //Suppose the employee who controls discovered the change 2 months after
        ContratoTerceirizado contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractAdictiveLocalDate)
                .criadoEm(fromLocalDate(dateContractAdictiveLocalDate.plusMonths(2))) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(new BigDecimal("11.000"))
                .cargo("Cargo 1")
                .build();

        LocalDate dateAditiveIncGrupA = LocalDate.of(2022, 6, 1);

        List<IncGrupoAContrato> incGrupoAContratoes = new ArrayList<>();
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("37.8")).data(dateAditiveIncGrupA).build()); //ordered by date desc
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("35.8")).data(dateContractLocalDate).build());

        liberacao = new Liberacao();
        liberacao.setTipo(TipoLiberacao.FERIAS);
        liberacao.setContratoTerceirizado(contratoTerceirizado);
        liberacao.setDataLiberacao(LocalDate.now());

        feriasLiberationHandler = new FeriasLiberationHandler(
                List.of(contratoTerceirizadoAditivo, contratoTerceirizado),
                incGrupoAContratoes
        );
    }

    @Test
    public void updateFuncionarioFeriasProvisionWithoutLiberationTest() {

        FeriasPeriodProvision funcionarioProvision = new FeriasPeriodProvision(new ArrayList<>());
        funcionarioProvision.setWorkPeriod(new WorkPeriod(LocalDate.of(2022,1,1), LocalDate.now()));
        funcionarioProvision.setFuncionario(Funcionario.builder().matricula("funcionario1").build());
        feriasLiberationHandler.updateFuncionarioProvision(funcionarioProvision);
        List<FeriasProvision> feriasProvisionList = funcionarioProvision.getFeriasProvisionList()
                .stream().filter(feriasProvision -> feriasProvision.getDataLiberacao() != null).toList();
        assertTrue(feriasProvisionList.isEmpty());
    }

    @Test
    public void updateFuncionarioFeriasProvisionWithLiberationTest() {

        FeriasPeriodProvision funcionarioProvision = new FeriasPeriodProvision(List.of(liberacao));
        funcionarioProvision.setWorkPeriod(new WorkPeriod(LocalDate.of(2022,1,1), LocalDate.now()));
        funcionarioProvision.setFuncionario(Funcionario.builder().matricula("funcionario1").build());
        feriasLiberationHandler.updateFuncionarioProvision(funcionarioProvision);
        assertFalse(funcionarioProvision.getFeriasProvisionList().isEmpty());
    }

    @Test
    public void addFuncionarioProvisionInsertNewTest() {

        feriasLiberationHandler.mapMatriculaLiberations(List.of(liberacao));
        feriasLiberationHandler.addFuncionarioProvision("funcionario1", LocalDate.now());
        assertFalse(feriasLiberationHandler.getFuncionarioProvisionsList().isEmpty());
    }

    @Test
    public void calcOnlyOneLiberationTest() {

        FeriasPeriodProvision feriasPeriodProvisions = feriasLiberationHandler.calc(
                List.of(liberacao),
                LocalDate.now()
        ).get(0);
        List<FeriasProvision> feriasProvisionList = feriasPeriodProvisions.getFeriasProvisionList()
                        .stream().filter(feriasProvision -> feriasProvision.getDataLiberacao() != null).toList();
        assertEquals(1, feriasProvisionList.size());
    }

    @Test
    @DisplayName("The professional has 2 full years of work to claim his vocation.")
    public void calcTwoFullYearsLiberationTest() {

        FeriasPeriodProvision feriasPeriodProvisions = feriasLiberationHandler.calc(
                List.of(liberacao, liberacao),
                LocalDate.of(2023, 12, 8)
        ).get(0);
        List<FeriasProvision> feriasProvisionList = feriasPeriodProvisions.getFeriasProvisionList()
                .stream().filter(feriasProvision -> feriasProvision.getDataLiberacao() != null).toList();
        assertEquals(2, feriasProvisionList.size());
    }

    @Test
    @DisplayName("Register 3 liberation but the professional has only 1 year of work, should return only 1 full year.")
    public void calcTest() {

        final LocalDate until =LocalDate.of(2022, 10, 30);
        final FeriasPeriodProvision feriasPeriodProvisions = feriasLiberationHandler.calc(
                List.of(liberacao, liberacao, liberacao),
                until
        ).get(0);

        assertEquals(1, feriasPeriodProvisions.getFeriasProvisionList().size());
    }

    @Test
    @DisplayName("Somehow, exists a liberation of an not existent funcionario.")
    public void calcLiberationOfNotFuncionarioNotExistent() {

        Liberacao liberacao = Liberacao.builder()
                .contratoTerceirizado(ContratoTerceirizado.builder()
                        .funcionario(Funcionario.builder()
                                .matricula("funcionarioNotExistent").build())
                        .contrato(Contrato.builder()
                                .numero("numeroContrato")
                                .build())
                        .build())
                .build();
        List<FeriasPeriodProvision> funcionarioProvisionsList = feriasLiberationHandler.calc(
                List.of(liberacao,liberacao,liberacao),
                LocalDate.of(2022, 12, 30)
        ).stream().filter(
                feriasPeriodProvision -> feriasPeriodProvision.getFuncionario().getMatricula().equals("funcionarioNotExistent")
        ).toList();
        assertTrue(funcionarioProvisionsList.isEmpty());
    }
}