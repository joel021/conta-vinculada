package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idPessoa;

    @Column(length = 60)
    public String nome;

    public Pessoa(Integer idPessoa, String nome) {
        this.idPessoa = idPessoa;
        this.nome = nome;
    }

    public Pessoa() {}

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
