package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContaBloqueada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaBloqueadaRepository extends JpaRepository<ContaBloqueada, Integer> {
}
