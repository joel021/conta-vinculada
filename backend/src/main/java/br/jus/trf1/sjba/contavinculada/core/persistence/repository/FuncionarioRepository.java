package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    Optional<Funcionario> findByMatricula(String matricula);

    List<Funcionario> findByPessoaFisica(PessoaFisica nome);

    @Query("select f from Funcionario f " +
            "where " +
            "f.deletadoEm is null " +
            "and f.pessoaFisica.cpf like ?1%")
    List<Funcionario> findNonDeleletedByCpfStartsWith(String cpf);
}
