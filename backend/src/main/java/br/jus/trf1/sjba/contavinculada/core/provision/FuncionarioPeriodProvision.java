package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public abstract class FuncionarioPeriodProvision {

    final List<Liberacao> liberacaoList = new ArrayList<>();
    private WorkPeriod workPeriod;
    private Funcionario funcionario;
    private Integer contratoTerceirizadoId;
    BigDecimal totalProvision;
    BigDecimal totalLiberation;

    public FuncionarioPeriodProvision(List<Liberacao> liberacaoList) {

        if (liberacaoList != null) {
            this.liberacaoList.addAll(liberacaoList);
            this.liberacaoList.sort(new Comparator<Liberacao>() {
                @Override
                public int compare(Liberacao o1, Liberacao o2) {
                    return o1.compare(o2);
                }
            });
        }
    }
    public abstract void revertLiberations();

    public abstract void updateTotalProvisionsAndLiberations();

    public abstract void addProvision(LocalDate date, Provision provision);

    public WorkPeriod getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(WorkPeriod workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Integer getContratoTerceirizadoId() {
        return contratoTerceirizadoId;
    }

    public void setContratoTerceirizadoId(Integer contratoTerceirizadoId) {
        this.contratoTerceirizadoId = contratoTerceirizadoId;
    }

    public BigDecimal getTotalProvision() {
        return totalProvision;
    }

    public void setTotalProvision(BigDecimal totalProvision) {
        this.totalProvision = totalProvision;
    }

    public BigDecimal getTotalLiberation() {
        return totalLiberation;
    }

    public void setTotalLiberation(BigDecimal totalLiberation) {
        this.totalLiberation = totalLiberation;
    }
}
