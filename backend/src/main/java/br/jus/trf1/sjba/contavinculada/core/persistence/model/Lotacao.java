package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Lotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idLotacao;

    public String descricao;

}
