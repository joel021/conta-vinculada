package br.jus.trf1.sjba.contavinculada.core.liberation.data;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FGTSProvision;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class FGTSContratoLiberacao {

    private Contrato contrato;
    private List<FGTSProvision> funcionarioProvisions;
    private BigDecimal totalProvision = BigDecimal.ZERO;
    private BigDecimal totalLiberation = BigDecimal.ZERO;

    public void setFuncionarioProvisions(List<FGTSProvision> funcionarioProvisions) {
        this.funcionarioProvisions = funcionarioProvisions;
        calcTotalProvisionAndLiberation();
    }

    public void calcTotalProvisionAndLiberation() {
        totalProvision = BigDecimal.ZERO;
        totalLiberation = BigDecimal.ZERO;

        if (funcionarioProvisions == null) {
            return;
        }

        for (FGTSProvision funcionarioProvision : funcionarioProvisions) {
            totalProvision = totalProvision.add(funcionarioProvision.getTotalProvision());
            totalLiberation = totalLiberation.add(funcionarioProvision.getTotalLiberation());
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

    public FGTSContratoLiberacao totalProvision(BigDecimal totalProvision) {
        this.totalProvision = totalProvision;
        return this;
    }
}