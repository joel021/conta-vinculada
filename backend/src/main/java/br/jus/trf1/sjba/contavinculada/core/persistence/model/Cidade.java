package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cidade {

    @Id
    public Integer idCidade;

    public String cidade;

    public String estado;

    public String sigla;

}
