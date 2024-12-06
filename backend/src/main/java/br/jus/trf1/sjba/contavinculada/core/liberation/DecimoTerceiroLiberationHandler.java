package br.jus.trf1.sjba.contavinculada.core.liberation;

import br.jus.trf1.sjba.contavinculada.core.liberation.data.WorkPeriod;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.provision.*;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

@Getter
public class DecimoTerceiroLiberationHandler extends FuncionarioLiberationMapper {

    private List<DecimoTerceiroPeriodProvision> funcionarioPeriodProvisions;

    public DecimoTerceiroLiberationHandler(List<ContratoTerceirizado> contratoTerceirizadoHistoryList,
                                           List<IncGrupoAContrato> incGrupoAContratoInc) throws NotAcceptableException {
        super(contratoTerceirizadoHistoryList, incGrupoAContratoInc);
        funcionarioPeriodProvisions = new ArrayList<>();
    }

    public List<DecimoTerceiroPeriodProvision> calc(List<Liberacao> liberacaoDecimoList, LocalDate until) {

        funcionarioPeriodProvisions = new ArrayList<>();
        mapMatriculaLiberations(liberacaoDecimoList);

        for (String matricula: funcionarioContratoMapper.getContratoTerceirzadoMap().keySet()) {
            addFuncionarioProvision(matricula, until);
        }

        return funcionarioPeriodProvisions;
    }

    public void addFuncionarioProvision(String matricula, LocalDate until) {

        final ContratoTerceirizado contratoTerceirizado = funcionarioContratoMapper.firstContract(matricula);
        final LocalDate endDate = funcionarioContratoMapper.getEndDate(matricula, until);

        DecimoTerceiroPeriodProvision periodProvision = new DecimoTerceiroPeriodProvision(matriculaLiberations.get(matricula));

        periodProvision.setFuncionario(contratoTerceirizado.getFuncionario());
        periodProvision.setWorkPeriod(new WorkPeriod(contratoTerceirizado.getDataInicio(), endDate));
        periodProvision.setContratoTerceirizadoId(funcionarioContratoMapper.lastContract(matricula).getId());

        updateFuncionarioProvision(periodProvision);

        funcionarioPeriodProvisions.add(periodProvision);
    }


}
