package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.utils.DateUtils;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromCalendar;

@Getter
public class ProvisionCalc {

    private final BigDecimal RATE_DECIMO = new BigDecimal("0.0909");
    private final BigDecimal RATE_FERIAS = new BigDecimal("0.0909");
    private final BigDecimal RATE_AB_FERIAS = new BigDecimal("0.0303");
    private final BigDecimal RATE_MULTA_FGTS = new BigDecimal("0.0349");
    private final BigDecimal PER_INC_GRUPO_A;

    public ProvisionCalc(BigDecimal perIncGrupoA) {
        PER_INC_GRUPO_A = perIncGrupoA;
    }

    public boolean canCal(LocalDate dataInicio, LocalDate date) {

        final WorkPeriod workPeriod = new WorkPeriod(dataInicio, date);

        if (DateUtils.equalsOrBefore(date, dataInicio) || workPeriod.endDayGreaterThanFifteen() == 0) {
            return false;
        }

        if (DateUtils.sameMonth(dataInicio, date)) {
            return workPeriod.firstMonthFifteen() == 1;
        }

        return true;
    }

    public Provision fromContratoTerceirizado(ContratoTerceirizado contratoTerceirizado, LocalDate endDate) {
        BigDecimal rateIncGrupoA = PER_INC_GRUPO_A.divide(
                BigDecimal.valueOf(100.0),
                10,
                RoundingMode.HALF_UP
        );
        final Provision provision = new Provision(RATE_DECIMO, RATE_FERIAS, RATE_AB_FERIAS,
                RATE_MULTA_FGTS, rateIncGrupoA);

        provision.setDate(endDate);
        provision.setCargo(contratoTerceirizado.getCargo());
        provision.setRemuneracao(contratoTerceirizado.getRemuneracao());
        provision.setInicioVigencia(contratoTerceirizado.getDataInicio());
        provision.setCriadoEm(fromCalendar(contratoTerceirizado.getCriadoEm()));

        if (!canCal(contratoTerceirizado.getDataInicio(), endDate)) {
            provision.setFerias(new BigDecimal("0"));
            provision.setDecimo(new BigDecimal("0"));
            provision.setMultaFGTS(new BigDecimal("0"));
            provision.setAbFerias(new BigDecimal("0"));
            provision.setIncGrupoA(new BigDecimal("0"));
        }

        return provision;
    }

}
