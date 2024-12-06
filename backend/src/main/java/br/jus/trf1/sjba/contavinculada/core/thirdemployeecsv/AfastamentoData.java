package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Afastamento;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AfastamentoData extends Afastamento {

    private String cpfSubstituido;

    private String cpfSubstituto;

    private String numeroContrato;

    private String nomeSubstituto;
}
