package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FeriasProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


public class FeriasPeriodProvision extends FuncionarioPeriodProvision {

    private final List<FeriasProvision> feriasProvisionList = new ArrayList<>();
    private final Set<String> monthStack = new HashSet<>();

    public FeriasPeriodProvision(List<Liberacao> liberacaoList) {
        super(liberacaoList);
    }

    @Override
    public void revertLiberations() {
        Collections.reverse(feriasProvisionList);
    }

    @Override
    public void updateTotalProvisionsAndLiberations() {

        totalLiberation = BigDecimal.ZERO;
        totalProvision = BigDecimal.ZERO;

        if (feriasProvisionList == null) {
            return;
        }

        for (FeriasProvision feriasProvision : feriasProvisionList) {
            totalLiberation = totalLiberation.add(feriasProvision.getTotalLiberation());
            totalProvision = totalProvision.add(feriasProvision.getTotalProvision());
        }
    }

    public void addProvision(LocalDate date, Provision provision) {

        provision.setDate(date);
        filterFerias(provision);

        if (alreadyInserted(date)) {
            return;
        }

        if (!feriasProvisionList.isEmpty()) {
            FeriasProvision feriasProvision = feriasProvisionList.get(feriasProvisionList.size() - 1);

            if (feriasProvision.getProvisoes().size() < 12) {
                feriasProvision.addProvision(provision);
                updateTotalProvisionsAndLiberations();
                return;
            }
        }
        createNewFeriasProvision(provision);
        updateTotalProvisionsAndLiberations();
    }

    public boolean alreadyInserted(LocalDate date) {

        final String yearMonth = date.getYear()+"-"+date.getMonth();
        if (monthStack.contains(yearMonth)) {
            return true;
        }
        monthStack.add(yearMonth);
        return false;
    }

    public void createNewFeriasProvision(Provision provision) {

        final FeriasProvision feriasProvision = new FeriasProvision();
        feriasProvision.addProvision(provision);
        feriasProvisionList.add(feriasProvision);

        updateProvisionStatus(feriasProvision);
    }

    public void updateProvisionStatus(FeriasProvision feriasProvision) {

        final int provisionsListSize = feriasProvisionList.size();
        if (provisionsListSize <= liberacaoList.size()) {
            feriasProvision.setLiberacao(liberacaoList.get(provisionsListSize - 1));
        }
    }

    public List<FeriasProvision> getFeriasProvisionList() {
        return feriasProvisionList;
    }

    public Provision filterFerias(Provision provision) {

        provision.setDecimo(new BigDecimal("0"));
        provision.setMultaFGTS(new BigDecimal("0"));

        return provision;
    }
}
