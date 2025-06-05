package br.jus.trf1.sjba.contavinculada.security.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsuarioTests {

    @Test
    public void getPapeisTest() {

        Usuario usuario = new Usuario();
        usuario.setPapeis(List.of(Papel.ROLE_ADMIN));
        assertTrue(usuario.getPapeis().contains(Papel.ROLE_MANAGER));
    }

    @Test
    public void getPapeisHasAdminTest() {

        Usuario usuario = new Usuario();
        usuario.setPapeis(List.of(Papel.ROLE_ADMIN));
        assertTrue(usuario.getPapeis().contains(Papel.ROLE_GUEST));
    }

    @Test
    public void getPapeisHasManagerTest() {

        Usuario usuario = new Usuario();
        usuario.setPapeis(List.of(Papel.ROLE_MANAGER));
        assertTrue(usuario.getPapeis().contains(Papel.ROLE_GUEST));
    }

    @Test
    public void getPapeisHasNotManagerTest() {

        Usuario usuario = new Usuario();
        usuario.setPapeis(List.of(Papel.ROLE_GUEST));
        assertFalse(usuario.getPapeis().contains(Papel.ROLE_MANAGER));
    }

    @Test
    public void getPapeisHasNotAdminTest() {

        Usuario usuario = new Usuario();
        usuario.setPapeis(List.of(Papel.ROLE_MANAGER));
        assertFalse(usuario.getPapeis().contains(Papel.ROLE_ADMIN));
    }

}
