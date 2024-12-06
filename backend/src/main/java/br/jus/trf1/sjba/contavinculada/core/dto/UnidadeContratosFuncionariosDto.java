package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UnidadeContratosFuncionariosDto {

    List<List<FuncionarioContratosDto>> funcionarioContratosDtoList = new ArrayList<>();

    public static UnidadeContratosFuncionariosDto fromContratoTerceirizadoList(List<ContratoTerceirizado> contratoTerceirizadoList) {

        UnidadeContratosFuncionariosMapper contratosFuncionariosMapper = new UnidadeContratosFuncionariosMapper(contratoTerceirizadoList);
        return contratosFuncionariosMapper.getUnidadeContratosFuncionariosDto();
    }
}
