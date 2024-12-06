package br.jus.trf1.sjba.contavinculada.core.provision.data;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ContratoProvisions {

    private Contrato contrato;
    private List<FuncionarioProvision> funcionarioProvisionList = new ArrayList<>();
}
