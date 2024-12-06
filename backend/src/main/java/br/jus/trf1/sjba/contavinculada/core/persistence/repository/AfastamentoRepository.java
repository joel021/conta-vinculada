package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Afastamento;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AfastamentoRepository extends JpaRepository<Afastamento, Integer> {


    List<Afastamento> findBySubstituidoAndDataInicio(ContratoTerceirizado contratoTerceirizado, LocalDate dataInicio);
}
