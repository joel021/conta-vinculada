package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratosTerceirizadosData {

    private List<ContratoTerceirizado> contratoTerceirizadoList;
    private List<AfastamentoData> afastamentoList;
}
