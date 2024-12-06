package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class ObjetoContrato {

    @Id
    public Integer idObjetoContrato;
    public String descricao;

}
