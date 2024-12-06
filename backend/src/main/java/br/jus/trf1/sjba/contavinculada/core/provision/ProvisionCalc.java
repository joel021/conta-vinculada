package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.utils.DateUtils;
import lombok.Getter;

import java.time.LocalDate;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromCalendar;

@Getter
public class ProvisionCalc {

    private final double RATE_DECIMO = 0.0909d;
    private final double RATE_FERIAS = 0.0909d;
    private final double RATE_AB_FERIAS = 0.0303d;
    private final double RATE_MULTA_FGTS = 0.0349d;
    private final double PER_INC_GRUPO_A;

    public ProvisionCalc(double perIncGrupoA) {
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

        final Provision provision = new Provision(PER_INC_GRUPO_A / 100.0d);

        provision.setDate(endDate);
        provision.setCargo(contratoTerceirizado.getCargo());
        provision.setRemuneracao(contratoTerceirizado.getRemuneracao());
        provision.setInicioVigencia(contratoTerceirizado.getDataInicio());
        provision.setCriadoEm(fromCalendar(contratoTerceirizado.getCriadoEm()));

        if (!canCal(contratoTerceirizado.getDataInicio(), endDate)) {
            provision.setFerias(0);
            provision.setDecimo(0);
            provision.setMultaFGTS(0);
            provision.setAbFerias(0);
            provision.setIncGrupoA(0);
        }

        return provision;
    }

}
