package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {
    Optional<PessoaFisica> findByCpf(String cpf);

    @Query(value = "select * from " +
            "pessoa_fisica pf, pessoa p " +
            "where p.nome like %:nome% " +
            "and pf.id_pessoa = p.id_pessoa " +
            "limit 10;", nativeQuery = true)
    List<PessoaFisica> findByNomeContainsLimited(@Param("nome") String nome);
}
