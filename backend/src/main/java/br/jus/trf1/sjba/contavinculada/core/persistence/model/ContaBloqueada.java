package br.jus.trf1.sjba.contavinculada.core.persistence.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class ContaBloqueada {

    @Id
    public Integer id;

    public String agencia;

    public String banco;

    public String conta;

    public Date dataEncerramento;

    public String numeroSei;

    @OneToOne
    @JoinColumn(name="idContrato", referencedColumnName = "idContrato")
    public Contrato contrato;

}
