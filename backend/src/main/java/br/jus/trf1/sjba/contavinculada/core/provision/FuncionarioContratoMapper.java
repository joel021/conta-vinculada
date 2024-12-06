package br.jus.trf1.sjba.contavinculada.core.provision;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FuncionarioContratoMapper {

    private Map<String, List<ContratoTerceirizado>> contratoTerceirzadoMap;

    public FuncionarioContratoMapper(List<ContratoTerceirizado> contratoTerceirizadoList) {
        updateMap(contratoTerceirizadoList);
    }

    public List<ContratoTerceirizado> getHistory(String matricula) {
        return contratoTerceirzadoMap.get(matricula);
    }

    public void updateMap(List<ContratoTerceirizado> contratoTerceirizadoList) {

        if (contratoTerceirizadoList == null) {
            return;
        }

        contratoTerceirzadoMap = new HashMap<>();

        for (ContratoTerceirizado contratoTerceirizado: contratoTerceirizadoList) {
            final String matricula = contratoTerceirizado.getFuncionario().getMatricula();
            contratoTerceirzadoMap.computeIfAbsent(matricula, k -> new ArrayList<>());
            contratoTerceirzadoMap.get(matricula).add(contratoTerceirizado);
        }
    }

    public ContratoTerceirizado firstContract(String matricula) {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirzadoMap.get(matricula);
        final int lastPosition = contratoTerceirizadoList.size() - 1;
        return contratoTerceirizadoList.get(lastPosition);
    }

    public ContratoTerceirizado lastContract(String matricula) {
        return contratoTerceirzadoMap.get(matricula).get(0);
    }

    public LocalDate getEndDate(String matricula, LocalDate provided) {

        final ContratoTerceirizado lastContratoTerceirizado = contratoTerceirzadoMap.get(matricula).get(0);
        if (lastContratoTerceirizado.getDataDesligamento() != null
                && provided.isAfter(lastContratoTerceirizado.getDataDesligamento())) {
            return lastContratoTerceirizado.getDataDesligamento();
        }
        return provided;
    }

}
