package br.jus.trf1.sjba.contavinculada.core.provision.data;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.OficioMovimentacao;

import java.time.LocalDate;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.*;


public class FeriasProvision extends Provisions {

    private WorkPeriod workPeriod;
    private LocalDate dataLiberacao;
    private LocalDate endDate;
    private double totalLiberation;
    private OficioMovimentacao oficioMovimentacao;

    public void addProvision(Provision provision) {

        if (super.getProvisoes().isEmpty()) {
            instanciatePeriod(provision);
        } else {
            updateEndDate(provision);
        }
        super.addProvision(provision);
        updateTotalLiberation();
    }

    public void instanciatePeriod(Provision provision) {
        endDate = provision.getDate();
        LocalDate firstDayOfMonthCurrentDate = getMinDateOfMonth(provision.getDate());
        if (equalsOrAfter(firstDayOfMonthCurrentDate, provision.getInicioVigencia())) {
            workPeriod = new WorkPeriod(firstDayOfMonthCurrentDate , provision.getDate());
        } else {
            workPeriod = new WorkPeriod(provision.getInicioVigencia(), provision.getDate());
        }
    }

    public void updateEndDate(Provision provision) {

        if (equalsOrAfter(provision.getDate(), endDate)) {
            endDate = provision.getDate();
        }
        workPeriod.setEndDate(endDate);
    }

    public WorkPeriod getWorkPeriod() {
        return workPeriod;
    }

    public LocalDate getDataLiberacao() {
        return dataLiberacao;
    }

    public void setLiberacao(Liberacao liberacao) {
        this.dataLiberacao = liberacao.getDataLiberacao();
        this.oficioMovimentacao = liberacao.getOficioMovimentacao();

        updateTotalLiberation();
    }

    public void updateTotalLiberation() {
        if (dataLiberacao != null) {
            this.totalLiberation = getTotalProvision();
        }
    }

    public double getTotalLiberation() {
        return totalLiberation;
    }

    public OficioMovimentacao getOficioMovimentacao() {
        return oficioMovimentacao;
    }
}
