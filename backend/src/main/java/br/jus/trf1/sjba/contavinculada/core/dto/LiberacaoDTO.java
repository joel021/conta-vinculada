package br.jus.trf1.sjba.contavinculada.core.dto;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LiberacaoDTO extends Liberacao {

    private LocalDate dataDesligamento;

    public Liberacao liberacao() {
        return Liberacao.builder()
                .contratoTerceirizado(contratoTerceirizado)
                .idLiberacao(idLiberacao)
                .tipo(tipo)
                .criadoEm(criadoEm)
                .criadoPor(criadoPor)
                .deletadoPor(deletadoPor)
                .deletadoEm(deletadoEm)
                .dataLiberacao(dataLiberacao)
                .oficioMovimentacao(oficioMovimentacao)
                .build();
    }
}
