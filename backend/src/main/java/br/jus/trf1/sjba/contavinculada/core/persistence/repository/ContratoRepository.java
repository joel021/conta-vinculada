package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {

    List<Contrato> findAllByPessoaJuridicaAndUnidadeContaining(Sort fimVigencia, PessoaJuridica pessoaJuridica, String unidade);

    List<Contrato> findByNumeroAndUnidadeContaining(String numero, String unidade);

    Optional<Contrato> findByIdContratoAndUnidadeContaining(Integer contratoId, String unidade);

    Optional<Contrato> findByNumero(String numero);
}
