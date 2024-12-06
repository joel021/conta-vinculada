package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class UnidadeContratosFuncionariosMapper {

    private UnidadeContratosFuncionariosDto unidadeContratosFuncionariosDto;

    private final HashMap<Integer, List<ContratoTerceirizado>> groupFuncionarioByContrato = new HashMap<>();

    public UnidadeContratosFuncionariosMapper(List<ContratoTerceirizado> contratoTerceirizadoList) {
        groupContratoTerceirizadoByContrato(contratoTerceirizadoList);
        buildUnidadeContratosFuncionariosDto();
    }

    public HashMap<Integer, List<ContratoTerceirizado>> groupContratoTerceirizadoByContrato(List<ContratoTerceirizado> contratoTerceirizadoList) {

        for(ContratoTerceirizado contratoTerceirizado: contratoTerceirizadoList) {

            final int idContrato = contratoTerceirizado.getContrato().getIdContrato();
            groupFuncionarioByContrato.computeIfAbsent(idContrato, k -> new ArrayList<>());
            groupFuncionarioByContrato.get(idContrato).add(contratoTerceirizado);
        }
        return groupFuncionarioByContrato;
    }

    public void buildUnidadeContratosFuncionariosDto() {

        unidadeContratosFuncionariosDto = new UnidadeContratosFuncionariosDto();

        for (int idContrato: groupFuncionarioByContrato.keySet()) {

            List<FuncionarioContratosDto> funcionarioContratoList = new FuncionarioContratosMapper(groupFuncionarioByContrato.get(idContrato))
                    .funcionarioContratosDtoList().stream().toList();
            unidadeContratosFuncionariosDto.getFuncionarioContratosDtoList().add(funcionarioContratoList);
        }
    }
}
