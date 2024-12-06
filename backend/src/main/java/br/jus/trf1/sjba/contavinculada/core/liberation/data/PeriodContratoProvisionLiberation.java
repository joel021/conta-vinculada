package br.jus.trf1.sjba.contavinculada.core.liberation.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PeriodContratoProvisionLiberation {

    Contrato contrato;
    double totalProvision;
    double totalLiberation;

    public abstract PeriodContratoProvisionLiberation contrato(Contrato contrato);

    public abstract void calcTotalProvisionLiberation();
}
