package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface IncGrupoAContratoRepository extends JpaRepository<IncGrupoAContrato, Integer> {

    List<IncGrupoAContrato> findByContratoAndDeletadoEm(Contrato contrato, Calendar deletadoEm);

    List<IncGrupoAContrato> findByContratoAndDeletadoEmOrderByDataDesc(Contrato contrato, Calendar deletadoEm);
}
