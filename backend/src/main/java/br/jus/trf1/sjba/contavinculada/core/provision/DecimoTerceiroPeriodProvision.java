package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.DecimoProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class DecimoTerceiroPeriodProvision extends FuncionarioPeriodProvision {

    private final HashMap<Integer, DecimoProvision> yearDecimoProvisions = new HashMap<>();

    public DecimoTerceiroPeriodProvision(List<Liberacao> liberacaoList) {
        super(liberacaoList);
    }

    @Override
    public void addProvision(LocalDate date, Provision provision) {

        DecimoProvision currentYearDecimoProvision = yearDecimoProvisions.get(date.getYear());
        if (currentYearDecimoProvision == null) {
            currentYearDecimoProvision = new DecimoProvision();
            yearDecimoProvisions.put(date.getYear(), currentYearDecimoProvision);
        }
        currentYearDecimoProvision.addProvision(filterToDecimo(provision));
        currentYearDecimoProvision.setWorkPeriod(provisionPeriod(date));

        if (yearDecimoProvisions.size() <= liberacaoList.size()) {
            currentYearDecimoProvision.setLiberacao(liberacaoList.get(yearDecimoProvisions.size() - 1));
        }
        updateTotalProvisionsAndLiberations();
    }

    public WorkPeriod provisionPeriod(LocalDate endDate) {

        LocalDate startDate;
        if (endDate.getYear() > getWorkPeriod().getStartDate().getYear()) {
            startDate = LocalDate.of(endDate.getYear(), 1,1);
        } else {
            startDate = getWorkPeriod().getStartDate();
        }

        return new WorkPeriod(startDate, endDate);
    }

    public Provision filterToDecimo(Provision provision) {

        provision.setFerias(new BigDecimal("0"));
        provision.setAbFerias(new BigDecimal("0"));
        provision.setMultaFGTS(new BigDecimal("0"));

        return provision;
    }

    @Override
    public void revertLiberations() {}

    @Override
    public void updateTotalProvisionsAndLiberations() {

        totalLiberation = BigDecimal.ZERO;
        totalProvision = BigDecimal.ZERO;
        for (Integer year : yearDecimoProvisions.keySet()) {
            totalLiberation = totalLiberation.add(yearDecimoProvisions.get(year).getTotalLiberation());
            totalProvision = totalProvision.add(yearDecimoProvisions.get(year).getTotalProvision());
        }
    }

    public HashMap<Integer, DecimoProvision> getYearDecimoProvisions() {
        return yearDecimoProvisions;
    }
}
