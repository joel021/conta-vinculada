package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Liberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.TipoLiberacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LiberacaoRepository extends JpaRepository<Liberacao, Integer> {

    @Query("select l from Liberacao l " +
            "where " +
            "l.deletadoEm is null and " +
            "l.tipo = ?2 and " +
            "l.contratoTerceirizado.contrato = ?1")
    List<Liberacao> findNonDeletedByContratoAndTipoLiberacao(
            Contrato contrato,
            TipoLiberacao tipoLiberacao
    );
}
