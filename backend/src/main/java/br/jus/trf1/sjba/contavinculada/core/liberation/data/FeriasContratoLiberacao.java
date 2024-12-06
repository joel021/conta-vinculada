package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.provision.FeriasPeriodProvision;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeriasContratoLiberacao extends PeriodContratoProvisionLiberation {

    private List<FeriasPeriodProvision> feriasPeriodProvisionList;

    @Override
    public FeriasContratoLiberacao contrato(Contrato contrato) {
        super.contrato = contrato;
        return this;
    }

    @Override
    public void calcTotalProvisionLiberation() {
        totalProvision = 0;
        totalLiberation = 0;

        if (feriasPeriodProvisionList == null || feriasPeriodProvisionList.isEmpty()) {
            return;
        }

        for (FeriasPeriodProvision feriasPeriodProvision: feriasPeriodProvisionList) {
            totalProvision += feriasPeriodProvision.getTotalProvision();
            totalLiberation += feriasPeriodProvision.getTotalLiberation();
        }
    }

    public FeriasContratoLiberacao feriasPeriodProvisionList(List<FeriasPeriodProvision> feriasPeriodProvisionList) {
        setFeriasPeriodProvisionList(feriasPeriodProvisionList);
        return this;
    }

    public void setFeriasPeriodProvisionList(List<FeriasPeriodProvision> feriasPeriodProvisionList) {
        this.feriasPeriodProvisionList = feriasPeriodProvisionList;
        calcTotalProvisionLiberation();
    }
}
