package br.jus.trf1.sjba.contavinculada.core.provision.data;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.OficioMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DecimoProvision extends Provisions {

    private LocalDate dataLiberacao;
    private BigDecimal totalLiberation;
    private OficioMovimentacao oficioMovimentacao;
    private WorkPeriod workPeriod;

    public void setLiberacao(Liberacao liberacao) {

        this.oficioMovimentacao = liberacao.getOficioMovimentacao();
        this.dataLiberacao = liberacao.getDataLiberacao();
        updatedTotalLiberation();
    }

    public void updatedTotalLiberation() {

        if (dataLiberacao == null) {
            totalLiberation = BigDecimal.ZERO;
        } else {
            totalLiberation = getTotalProvision();
        }
    }

    public void addProvision(Provision provision){
        super.addProvision(provision);
        updatedTotalLiberation();
    }
}
