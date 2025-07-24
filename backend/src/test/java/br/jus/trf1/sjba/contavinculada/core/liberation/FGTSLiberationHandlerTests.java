package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FGTSProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
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

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FGTSLiberationHandlerTests {

    private FGTSLiberationHandler fgtsLiberationHandler;
    private Liberacao liberacao;
    private ContratoTerceirizado contratoTerceirizadoAditivo;
    private ContratoTerceirizado contratoTerceirizado;
    private List<IncGrupoAContrato> incGrupoAContratoes;

    @BeforeEach
    public void setup() throws NotAcceptableException {

        LocalDate dateContractLocalDate = LocalDate.of(2021, 11, 8);
        LocalDate dateContractAdictiveLocalDate = LocalDate.of(2022,2, 1);

        Funcionario funcionario = Funcionario.builder().matricula("funcionario1").build();

        contratoTerceirizado = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractLocalDate)
                .criadoEm(fromLocalDate(dateContractLocalDate))
                .remuneracao(new BigDecimal("6600.0"))
                .cargo("ENGENHEIRO CIVIL carga horária 30h semanais")
                .build();

        LocalDate criadoEm = dateContractAdictiveLocalDate.plusMonths(2);
        //Suppose the employee who controls discovered the change 2 months after
        contratoTerceirizadoAditivo = ContratoTerceirizado.builder()
                .funcionario(funcionario)
                .dataInicio(dateContractAdictiveLocalDate)
                .criadoEm(fromLocalDate(criadoEm)) //Suppose the employee who controls discovered the change 2 months after
                .remuneracao(new BigDecimal("11.000"))
                .cargo("Cargo 1")
                .build();

        ContratoTerceirizado contratoTerceirizado1 = ContratoTerceirizado.builder()
                .funcionario(Funcionario.builder()
                        .matricula("matricula1")
                        .build())
                .dataInicio(LocalDate.of(2021,1,1))
                .criadoEm(Calendar.getInstance())
                .build();

        LocalDate dateAditiveIncGrupA = LocalDate.of(2022, 6, 1);

        incGrupoAContratoes = new ArrayList<>();
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("37.8")).data(dateAditiveIncGrupA).build()); //ordered by date desc
        incGrupoAContratoes.add(IncGrupoAContrato.builder().incGrupoA(new BigDecimal("35.8")).data(dateContractLocalDate).build());

        liberacao = new Liberacao();
        liberacao.setTipo(TipoLiberacao.DECIMO_TERCEIRO);
        liberacao.setContratoTerceirizado(contratoTerceirizado);

        fgtsLiberationHandler = new FGTSLiberationHandler(
                List.of(contratoTerceirizadoAditivo, contratoTerceirizado, contratoTerceirizado1),
                incGrupoAContratoes
        );
        fgtsLiberationHandler.mapMatriculaLiberations(new ArrayList<>());
    }

    @Test
    @DisplayName("Should filter to have only FGTS values.")
    public void filterToFGTSTest() {

        Provision provision = new Provision(
                new BigDecimal("0.0909"),
                new BigDecimal("0.0909"),
                new BigDecimal("0.0303"),
                new BigDecimal("0.0349"),
                new BigDecimal("0.358")
        );
        provision.setRemuneracao(new BigDecimal("6600"));
        final BigDecimal expected = new BigDecimal("230.34");

        fgtsLiberationHandler.filterToFGTS(provision);
        final BigDecimal result = provision.getTotalProvisaoMensal()
                .multiply(new BigDecimal("100"))
                .setScale(0, RoundingMode.CEILING)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should add provisions for all months of work of the employee.")
    public void updateFuncionarioProvisionTest() {

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 30);
        WorkPeriod workPeriod = new WorkPeriod(startDate, endDate);

        FGTSProvision funcionarioProvision = new FGTSProvision();
        funcionarioProvision.setWorkPeriod(workPeriod);
        funcionarioProvision.setFuncionario(Funcionario.builder().matricula("funcionario1").build());

        fgtsLiberationHandler.updateFuncionarioProvision(funcionarioProvision);
        assertEquals(workPeriod.getWorkMonths(), funcionarioProvision.getProvisoes().size());
    }

    @Test
    @DisplayName("dataDesligamento is null, should consider the until date.")
    public void addFuncionarioProvisionTest() {

        String matricula = "funcionario1";
        LocalDate until = LocalDate.of(2021, 12, 30);
        fgtsLiberationHandler.addFuncionarioProvision(matricula, until);

        assertEquals(
                2,
                fgtsLiberationHandler.getFuncionarioProvisionsMap().get(0).getProvisoes().size(),
                "Considering period of 2021-NOVEMBER-8 to 2021-DECEMBER-30, there are 2 months."
        );
    }

    @Test
    @DisplayName("The professional has no provisions because date is before he ever had, at least, one contract.")
    public void addFuncionarioNotProvisionTest() throws NotAcceptableException {

        contratoTerceirizadoAditivo.setDataDesligamento(LocalDate.of(2022, 3, 30));
        FGTSLiberationHandler fgtsLiberationHandler = new FGTSLiberationHandler(
                List.of(contratoTerceirizadoAditivo, contratoTerceirizado),
                incGrupoAContratoes);
        fgtsLiberationHandler.mapMatriculaLiberations(new ArrayList<>());

        String matricula = "funcionario1";
        LocalDate until = LocalDate.of(2020, 12, 30);
        fgtsLiberationHandler.addFuncionarioProvision(matricula, until);

        assertEquals(
                0,
                fgtsLiberationHandler.getFuncionarioProvisionsMap().get(0).getProvisoes().size(),
                "Considering period of 2021-NOVEMBER-8 to 2022-MARCH-30, there are 5 months."
        );
    }

    @Test
    @DisplayName("dataDelisgamento is not null, should consider it.")
    public void addFuncionarioProvisionOffTest() throws NotAcceptableException {

        contratoTerceirizadoAditivo.setDataDesligamento(LocalDate.of(2022, 3, 30));
        FGTSLiberationHandler fgtsLiberationHandler = new FGTSLiberationHandler(
                List.of(contratoTerceirizadoAditivo, contratoTerceirizado),
                incGrupoAContratoes);
        fgtsLiberationHandler.mapMatriculaLiberations(List.of(liberacao));

        String matricula = "funcionario1";
        LocalDate until = LocalDate.of(2022, 12, 30);
        fgtsLiberationHandler.addFuncionarioProvision(matricula, until);

        assertEquals(
                5,
                fgtsLiberationHandler.getFuncionarioProvisionsMap().get(0).getProvisoes().size(),
                "Considering period of 2021-NOVEMBER-8 to 2022-MARCH-30, there are 5 months."
        );
    }

    @Test
    @DisplayName("Theres some employees who never had liberation, the system should still calc for them.")
    public void calcForEmployeesWhoNeverHadLiberationTest() {

        List<FGTSProvision> fgtsProvisions = fgtsLiberationHandler.calc(List.of(liberacao), LocalDate.now());
        assertEquals(2, fgtsProvisions.size(), "Should include both of 2 employees.");
    }

}
