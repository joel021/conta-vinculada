package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Integer> {

    Optional<PessoaJuridica> findByCnpj(String cnpj);

    List<PessoaJuridica> findByNomeContains(Sort sort, String name);
}
