package br.jus.trf1.sjba.contavinculada.core.persistence.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Afastamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idAfastamento;

    @ManyToOne
    @JoinColumn(name="id_contrato_substituido", referencedColumnName = "id")
    public ContratoTerceirizado substituido;

    @ManyToOne
    @JoinColumn(name="id_contrato_substituto", referencedColumnName = "id")
    public ContratoTerceirizado substituto;

    public LocalDate dataInicio;

    public LocalDate dataFim;

}
