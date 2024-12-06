package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.FuncionarioContratoMapper;
import br.jus.trf1.sjba.contavinculada.core.provision.FuncionarioProvisionHistory;
import br.jus.trf1.sjba.contavinculada.core.provision.FuncionarioPeriodProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FuncionarioLiberationMapper {

    final FuncionarioProvisionHistory funcionarioProvisionHistory;
    final FuncionarioContratoMapper funcionarioContratoMapper;
    Map<String, List<Liberacao>> matriculaLiberations;

    public FuncionarioLiberationMapper(List<ContratoTerceirizado> contratoTerceirizadoHistoryList,
                                       List<IncGrupoAContrato> incGrupoAContratoInc) throws NotAcceptableException {

        this.funcionarioContratoMapper = new FuncionarioContratoMapper(contratoTerceirizadoHistoryList);
        this.funcionarioProvisionHistory = new FuncionarioProvisionHistory(incGrupoAContratoInc);
    }
    public void mapMatriculaLiberations(List<Liberacao> liberacaoFeriasList) {

        matriculaLiberations = new HashMap<>();

        for (Liberacao liberacao : liberacaoFeriasList) {
            final String matricula = liberacao.getContratoTerceirizado().getFuncionario().getMatricula();

            if (funcionarioContratoMapper.getContratoTerceirzadoMap().containsKey(matricula)) {
                matriculaLiberations.computeIfAbsent(matricula, k -> new ArrayList<>());
                matriculaLiberations.get(matricula).add(liberacao);
            }
        }
    }

    public void updateFuncionarioProvision(FuncionarioPeriodProvision funcionarioProvision) {

        PeriodWorkIterator periodWorkIterator = funcionarioProvision.getWorkPeriod().iterator();

        while (periodWorkIterator.hasNext()) {

            final LocalDate date = periodWorkIterator.next();
            List<Provision> provisionList = funcionarioProvisionHistory.calcFuncionarioProvision(
                    date,
                    funcionarioContratoMapper.getHistory(funcionarioProvision.getFuncionario().getMatricula())
            ).getProvisoes();
            if (provisionList.isEmpty()) {
                continue;
            }
            funcionarioProvision.addProvision(date, provisionList.get(0));
        }
        funcionarioProvision.revertLiberations();
    }

}