package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class FuncionarioContratosDto extends Funcionario {

    private Contrato contrato;

    @JsonManagedReference
    private List<ContratoTerceirizado> contratoTerceirizadoList = new ArrayList<>();

    public static FuncionarioContratosDto getInstanceFromFuncionario(Funcionario funcionario) {

        FuncionarioContratosDto funcionarioContratosDto = new FuncionarioContratosDto();
        funcionarioContratosDto.setContratoTerceirizadoList(new ArrayList<>());
        funcionarioContratosDto.setIdFuncionario(funcionario.getIdFuncionario());
        funcionarioContratosDto.setMatricula(funcionarioContratosDto.getMatricula());
        funcionarioContratosDto.setPessoaFisica(funcionario.getPessoaFisica());
        funcionarioContratosDto.setCriadoEm(funcionario.getCriadoEm());
        funcionarioContratosDto.setCriadoPor(funcionario.getCriadoPor());
        funcionarioContratosDto.setDeletadoEm(funcionario.getDeletadoEm());
        funcionarioContratosDto.setDeletadoPor(funcionario.getDeletadoPor());
        return funcionarioContratosDto;
    }

    public FuncionarioContratosDto sortDesc() {

        if (contratoTerceirizadoList == null) {
            return this;
        }
        contratoTerceirizadoList.sort(new Comparator<ContratoTerceirizado>() {
            @Override
            public int compare(ContratoTerceirizado c1, ContratoTerceirizado c2) {
                return c2.compare(c1);
            }
        });
        return this;
    }


}
