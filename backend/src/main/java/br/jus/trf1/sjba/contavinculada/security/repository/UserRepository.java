package br.jus.trf1.sjba.contavinculada.security.repository;


import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Usuario, Integer> {

    long countByPapeisContains(Papel papel);

    List<Usuario> findByPapeisContains(Papel papel);

    Optional<Usuario> findByUsuario(String usuario);

}
