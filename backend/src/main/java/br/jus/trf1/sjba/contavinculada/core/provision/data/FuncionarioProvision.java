package br.jus.trf1.sjba.contavinculada.core.provision.data;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.OficioMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FuncionarioProvision extends Provisions {

    private WorkPeriod workPeriod;
    private LocalDate dataLiberacao;
    private Funcionario funcionario;
    private double totalLiberation;
    private OficioMovimentacao oficioMovimentacao;

    public void setDataLiberacao(LocalDate dataLiberacao) {

        this.dataLiberacao = dataLiberacao;
        totalLiberation = 0;
        updateTotalLiberacao();
    }

    public void updateTotalLiberacao() {
        if (dataLiberacao != null) {
            totalLiberation = getTotalProvision();
        }
    }

    public void addProvision(Provision provision) {
        super.addProvision(provision);
        updateTotalLiberacao();
    }
}
