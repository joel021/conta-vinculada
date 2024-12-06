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
public class FeriasLiberationHandler extends FuncionarioLiberationMapper {

    private List<FeriasPeriodProvision> funcionarioProvisionsList;

    public FeriasLiberationHandler(List<ContratoTerceirizado> contratoTerceirizadoHistoryList,
                                   List<IncGrupoAContrato> incGrupoAContratoInc) throws NotAcceptableException {
        super(contratoTerceirizadoHistoryList, incGrupoAContratoInc);
        funcionarioProvisionsList = new ArrayList<>();
    }

    public List<FeriasPeriodProvision> calc(List<Liberacao> liberacaoFeriasList, LocalDate until) {

        funcionarioProvisionsList = new ArrayList<>();
        buildFuncionarioLiberation(liberacaoFeriasList, until);
        return funcionarioProvisionsList;
    }

    public void buildFuncionarioLiberation(List<Liberacao> liberacaoFeriasList, LocalDate until) {

        mapMatriculaLiberations(liberacaoFeriasList);

        for (String matricula : funcionarioContratoMapper.getContratoTerceirzadoMap().keySet()) {
            addFuncionarioProvision(matricula, until);
        }
    }

    public void addFuncionarioProvision(String matricula, LocalDate until) {

        final ContratoTerceirizado contratoTerceirizado = funcionarioContratoMapper.firstContract(matricula);
        FeriasPeriodProvision funcionarioProvision = new FeriasPeriodProvision(matriculaLiberations.get(matricula));
        funcionarioProvision.setFuncionario(contratoTerceirizado.getFuncionario());
        funcionarioProvision.setContratoTerceirizadoId(funcionarioContratoMapper.lastContract(matricula).getId());
        final LocalDate startDate = contratoTerceirizado.getDataInicio();
        final LocalDate endDate = funcionarioContratoMapper.getEndDate(matricula, until);
        funcionarioProvision.setWorkPeriod(new WorkPeriod(startDate, endDate));

        updateFuncionarioProvision(funcionarioProvision);
        funcionarioProvisionsList.add(funcionarioProvision);
    }

}