package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;

import java.util.*;

public class FuncionarioContratosMapper {

    private final Map<Integer, FuncionarioContratosDto> funcionarioContratosDtoHashMap = new HashMap<>();

    private List<ContratoTerceirizado> contratoTerceirizadoList;

    public FuncionarioContratosMapper(List<ContratoTerceirizado> contratoTerceirizadoList) {

        this.contratoTerceirizadoList = contratoTerceirizadoList;

        if (this.contratoTerceirizadoList == null) {
            this.contratoTerceirizadoList = new ArrayList<>();
        }
    }

    public void sortEachByDataInicio() {

        for (int funcionarioId: funcionarioContratosDtoHashMap.keySet()) {
            funcionarioContratosDtoHashMap.get(funcionarioId).sortDesc();
        }
    }

    public Collection<FuncionarioContratosDto> funcionarioContratosDtoList() {

        funcionarioContratosDtoHashMap.clear();

        for (ContratoTerceirizado contratoTerceirizado: contratoTerceirizadoList) {

            FuncionarioContratosDto funcionarioContratosDto = getFuncionarioContratosDtoFromMap(contratoTerceirizado);
            funcionarioContratosDto.getContratoTerceirizadoList().add(contratoTerceirizado);

            funcionarioContratosDtoHashMap.put(funcionarioContratosDto.getIdFuncionario(), funcionarioContratosDto);
        }
        sortEachByDataInicio();
        return funcionarioContratosDtoHashMap.values();
    }

    public FuncionarioContratosDto getFuncionarioContratosDtoFromMap(ContratoTerceirizado contratoTerceirizado) {

        FuncionarioContratosDto funcionarioContratosDto = funcionarioContratosDtoHashMap.get(
                contratoTerceirizado.getFuncionario().getIdFuncionario()
        );

        if (funcionarioContratosDto == null) {
            funcionarioContratosDto = FuncionarioContratosDto.getInstanceFromFuncionario(contratoTerceirizado.getFuncionario());
        }

        funcionarioContratosDto.setContrato(contratoTerceirizado.getContrato());
        contratoTerceirizado.setFuncionario(null);
        contratoTerceirizado.setContrato(null);
        return funcionarioContratosDto;
    }

}
