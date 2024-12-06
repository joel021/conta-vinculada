package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecaoJudiciariaRepository extends JpaRepository<SecaoJudiciaria, Integer> {
    List<SecaoJudiciaria> findBySigla(String sigla);
}
