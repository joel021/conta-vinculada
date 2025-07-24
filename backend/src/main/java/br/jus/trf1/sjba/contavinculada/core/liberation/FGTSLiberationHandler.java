package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FGTSProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.data.Provision;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FGTSLiberationHandler extends FuncionarioLiberationMapper {

    private Map<String, FGTSProvision> funcionarioProvisionsMap;

    public FGTSLiberationHandler(List<ContratoTerceirizado> contratoTerceirizadoHistoryList, List<IncGrupoAContrato> incGrupoAContratoInc)
            throws NotAcceptableException {
        super(contratoTerceirizadoHistoryList, incGrupoAContratoInc);
        funcionarioProvisionsMap = new HashMap<>();
    }

    public List<FGTSProvision> calc(List<Liberacao> liberacaoDecimoList, LocalDate until) {

        funcionarioProvisionsMap = new HashMap<>();
        mapMatriculaLiberations(liberacaoDecimoList);

        for (String matricula : funcionarioContratoMapper.getContratoTerceirzadoMap().keySet()) {
            addFuncionarioProvision(matricula, until);
        }

        return funcionarioProvisionsMap.values().stream().toList();
    }

    public void setLiberacao(FGTSProvision funcionarioFGTSProvision, String matricula) {

        if (matriculaLiberations.get(matricula) != null) {
            final Liberacao liberacao = matriculaLiberations.get(matricula).get(0);
            funcionarioFGTSProvision.setDataLiberacao(liberacao.getDataLiberacao());
            funcionarioFGTSProvision.setOficioMovimentacao(liberacao.getOficioMovimentacao());
        }
    }

    public void addFuncionarioProvision(String matricula, LocalDate until) {

        final var firstContratoTerceirizado = funcionarioContratoMapper.firstContract(matricula);
        final LocalDate endDate = funcionarioContratoMapper.getEndDate(matricula, until);
        final WorkPeriod workPeriod = new WorkPeriod(firstContratoTerceirizado.getDataInicio(), endDate);
        FGTSProvision funcionarioFGTSProvision = new FGTSProvision();
        funcionarioFGTSProvision.setWorkPeriod(workPeriod);
        funcionarioFGTSProvision.setFuncionario(firstContratoTerceirizado.getFuncionario());
        funcionarioFGTSProvision.setContratoTerceirizadoId(funcionarioContratoMapper.lastContract(matricula).getId());
        setLiberacao(funcionarioFGTSProvision, matricula);

        updateFuncionarioProvision(funcionarioFGTSProvision);

        funcionarioProvisionsMap.put(matricula, funcionarioFGTSProvision);
    }

    public void updateFuncionarioProvision(FGTSProvision funcionarioProvision) {

        final PeriodWorkIterator workIterator = funcionarioProvision.getWorkPeriod().iterator();
        while(workIterator.hasNext()) {
            List<Provision> provisionList = funcionarioProvisionHistory.calcFuncionarioProvision(
                    workIterator.next(),
                    funcionarioContratoMapper.getHistory(funcionarioProvision.getFuncionario().getMatricula())
            ).getProvisoes();
            if (provisionList.isEmpty()) {
                continue;
            }
            funcionarioProvision.addProvision(filterToFGTS(provisionList.get(0)));
        }
        Collections.reverse(funcionarioProvision.getProvisoes());
    }

    public List<FGTSProvision> getFuncionarioProvisionsMap() {
        return funcionarioProvisionsMap.values().stream().toList();
    }

    public Provision filterToFGTS(Provision provision) {

        provision.setDecimo(new BigDecimal("0"));
        provision.setFerias(new BigDecimal("0"));
        provision.setAbFerias(new BigDecimal("0"));

        return provision;
    }
}