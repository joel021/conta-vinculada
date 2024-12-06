package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public interface ContratoTerceirizadoRepository extends JpaRepository<ContratoTerceirizado, Integer> {

    @Query(value = """
    select ct from ContratoTerceirizado ct, Contrato c
                where
                    c.unidade = ?1
                    and ct.contrato = c
                    and ct.deletadoEm is null
                    and not ct.funcionario.matricula in (
                        select ct.funcionario.matricula as matricula from ContratoTerceirizado ct, Contrato c
                            where c.unidade = ?1
                            and ct.contrato = c
                            and ct.deletadoEm is null
                            and not dataDesligamento is null
                    )
                    order by c.inicioVigencia, ct.dataInicio DESC
    """)
    List<ContratoTerceirizado> findNotDeletedActiveByUnidadeOrderByInicioVigenciaDataInicioDesc(String unidade);

    @Query(value = """
    select ct from ContratoTerceirizado ct
                where
                    ct.contrato.idContrato = ?1
                    and ct.deletadoEm is null
                    and not ct.funcionario.matricula in (
                        select ct.funcionario.matricula as matricula from ContratoTerceirizado ct
                            where ct.contrato.idContrato = ?1
                            and ct.deletadoEm is null
                            and not dataDesligamento is null
                    )
                    order by ct.dataInicio DESC
    """)
    List<ContratoTerceirizado> findActiveByIdContratoAndBeforeDateOrderByDataInicioDesc(int idContrato);

    List<ContratoTerceirizado> findByFuncionarioAndContratoAndDeletadoEmOrderByDataInicioDesc(Funcionario funcionario,
                                                                                              Contrato contrato,
                                                                                              LocalDate deletadoEm);

    List<ContratoTerceirizado> findByFuncionarioAndContratoAndDataInicioAndDeletadoEm(
            Funcionario funcionario,
            Contrato contrato,
            LocalDate dataInicio,
            Calendar deletadoEm);

    List<ContratoTerceirizado> findByContratoAndDeletadoEmOrderByDataInicioDesc(
            Contrato contrato,
            LocalDate deletadoEm);
}
