package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.provision.DecimoTerceiroPeriodProvision;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DecimoContratoLiberacao extends PeriodContratoProvisionLiberation {

    private List<DecimoTerceiroPeriodProvision> funcionarioProvisions;

    @Override
    public DecimoContratoLiberacao contrato(Contrato contrato) {
        super.contrato = contrato;
        return this;
    }

    @Override
    public void calcTotalProvisionLiberation() {
        totalProvision = 0;
        totalLiberation = 0;

        if (funcionarioProvisions == null) {
            return;
        }

        for (DecimoTerceiroPeriodProvision decimoTerceiroPeriodProvision: funcionarioProvisions) {
            totalProvision += decimoTerceiroPeriodProvision.getTotalProvision();
            totalLiberation += decimoTerceiroPeriodProvision.getTotalLiberation();
        }
    }

    public void setFuncionarioProvisions(List<DecimoTerceiroPeriodProvision> funcionarioProvisions) {
        this.funcionarioProvisions = funcionarioProvisions;
        calcTotalProvisionLiberation();
    }

    public DecimoContratoLiberacao funcionarioProvisions(List<DecimoTerceiroPeriodProvision> funcionarioProvisions) {
        setFuncionarioProvisions(funcionarioProvisions);
        return this;
    }

}
