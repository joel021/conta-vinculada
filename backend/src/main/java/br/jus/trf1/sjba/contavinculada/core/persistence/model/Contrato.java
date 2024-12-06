package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idContrato;

    public String unidade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id_objeto_contrato", referencedColumnName = "idObjetoContrato")
    public ObjetoContrato objetoContrato;

    @ManyToOne
    @JoinColumn(name="id_pessoa")
    public PessoaJuridica pessoaJuridica;

    public LocalDate dataAssinatura;

    public LocalDate fimVigencia;

    public LocalDate inicioVigencia;

    @Column(unique = true)
    public String numero;

    @ManyToOne
    @JoinColumn(name="cnpj_secao")
    public SecaoJudiciaria secaoJudiciaria;

}
