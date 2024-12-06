package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FGTSProvision;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FGTSContratoLiberacao {

    private Contrato contrato;
    private List<FGTSProvision> funcionarioProvisions;
    private double totalProvision;
    private double totalLiberation;

    public void setFuncionarioProvisions(List<FGTSProvision> funcionarioProvisions) {
        this.funcionarioProvisions = funcionarioProvisions;
        calcTotalProvisionAndLiberation();
    }

    public void calcTotalProvisionAndLiberation() {
        totalProvision = 0;
        totalLiberation = 0;

        if (funcionarioProvisions == null) {
            return;
        }

        for (FGTSProvision funcionarioProvision: funcionarioProvisions) {
            totalProvision += funcionarioProvision.getTotalProvision();
            totalLiberation += funcionarioProvision.getTotalLiberation();
        }
    }

    public FGTSContratoLiberacao funcionarioProvisions(List<FGTSProvision> funcionarioProvisions) {
        this.setFuncionarioProvisions(funcionarioProvisions);
        return this;
    }

    public FGTSContratoLiberacao contrato(Contrato contrato) {
        this.setContrato(contrato);
        return this;
    }

    public FGTSContratoLiberacao totalProvision(double totalProvision) {
        this.totalProvision = totalProvision;
        return this;
    }

}
