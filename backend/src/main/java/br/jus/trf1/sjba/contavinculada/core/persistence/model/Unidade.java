package br.jus.trf1.sjba.contavinculada.core.persistence.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idUnidade;

    public String nomeUnidade;

    public String siglaUnidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_cidade", referencedColumnName = "idCidade")
    public Cidade cidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cnpj_secao", referencedColumnName = "cnpjSecao")
    public SecaoJudiciaria secaoJudiciaria;

}

