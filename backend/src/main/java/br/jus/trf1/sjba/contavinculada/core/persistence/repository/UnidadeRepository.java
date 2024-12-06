package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnidadeRepository extends JpaRepository<Unidade, Integer> {

    List<Unidade> findBySiglaUnidade(String siglaUnidade);

    List<Unidade> findBySiglaUnidadeAndSecaoJudiciaria(String siglaUnidade, SecaoJudiciaria secaoJudiciaria);
}
