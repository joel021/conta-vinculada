package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FuncionarioProvision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;

import java.time.LocalDate;
import java.util.*;

public class FuncionarioProvisionMapper {

    private final FuncionarioContratoMapper funcionarioContratoMapper;
    private final FuncionarioProvisionHistory funcionarioProvisionHistory;

    public FuncionarioProvisionMapper(List<ContratoTerceirizado> contratoTerceirizadoList,
                                      List<IncGrupoAContrato> incGrupoAContratoInc) throws NotAcceptableException {

        if (incGrupoAContratoInc == null || incGrupoAContratoInc.isEmpty()) {
            throw new NotAcceptableException("O sistema não pode calcular provisões sem o valor de Inc. grupo A.");
        }
        funcionarioContratoMapper = new FuncionarioContratoMapper(contratoTerceirizadoList);
        funcionarioProvisionHistory = new FuncionarioProvisionHistory(incGrupoAContratoInc);
    }

    public List<FuncionarioProvision> allFuncionarioToSpecificPeriod(LocalDate date) {

        List<FuncionarioProvision> funcionarioProvisions = new ArrayList<>();

        for (List<ContratoTerceirizado> contratoHistory: funcionarioContratoMapper.getContratoTerceirzadoMap().values()) {

            FuncionarioProvision funcionarioProvision = funcionarioProvisionHistory.calcFuncionarioProvision(date, contratoHistory);
            funcionarioProvisions.add(funcionarioProvision);
        }

        return funcionarioProvisions;
    }


}
