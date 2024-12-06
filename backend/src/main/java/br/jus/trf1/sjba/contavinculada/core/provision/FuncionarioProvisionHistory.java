package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FuncionarioProvision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;

import java.time.LocalDate;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.equalsOrAfter;

public class FuncionarioProvisionHistory {

    private final List<IncGrupoAContrato> incGrupoAContratoInc;

    public FuncionarioProvisionHistory(List<IncGrupoAContrato> historyIncGrupoA) throws NotAcceptableException {

        if (historyIncGrupoA == null || historyIncGrupoA.isEmpty()) {
            throw new NotAcceptableException("O sistema não pode calcular provisões sem o valor de Inc. grupo A.");
        }
        this.incGrupoAContratoInc = historyIncGrupoA;
    }

    public FuncionarioProvision calcFuncionarioProvision(LocalDate date, List<ContratoTerceirizado> historyContratoTerceirizado) {

        final ProvisionCalc provisionCalc = getProvisaoCalc(date);
        FuncionarioProvision funcionarioProvision = new FuncionarioProvision();
        funcionarioProvision.setFuncionario(historyContratoTerceirizado.get(0).getFuncionario());

        for (ContratoTerceirizado contratoTerceirizado : historyContratoTerceirizado) {

            if (equalsOrAfter(date, contratoTerceirizado.getDataInicio())) {
                funcionarioProvision.addProvision(provisionCalc.fromContratoTerceirizado(contratoTerceirizado, date));
            }
        }

        return funcionarioProvision;
    }

    public ProvisionCalc getProvisaoCalc(LocalDate date) {

        for (IncGrupoAContrato incGrupoAContrato: incGrupoAContratoInc) {

            if (equalsOrAfter(date, incGrupoAContrato.getData())) {
                return new ProvisionCalc(incGrupoAContrato.getIncGrupoA());
            }
        }

        return new ProvisionCalc(incGrupoAContratoInc.get(0).getIncGrupoA());
    }
}
