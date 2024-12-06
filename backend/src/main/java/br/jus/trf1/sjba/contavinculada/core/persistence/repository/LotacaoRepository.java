package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Lotacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LotacaoRepository extends JpaRepository<Lotacao, Integer> {

    List<Lotacao> findByDescricao(String descricao);
}
