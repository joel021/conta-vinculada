package br.jus.trf1.sjba.contavinculada.utils;

import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UsersFromJsonFileTests {


    @Test
    public void getUsersTest() throws NotFoundException {

        UsersFromJsonFile usersFromJsonFile = new UsersFromJsonFile();
        List<Usuario> userList = usersFromJsonFile.getUsers("test");
        assertFalse(userList.isEmpty());
    }

    @Test
    public void getUsersHasPapeisTest() throws NotFoundException {

        UsersFromJsonFile usersFromJsonFile = new UsersFromJsonFile();
        List<Usuario> userList = usersFromJsonFile.getUsers("test");
        assertFalse(userList.get(0).getPapeis().isEmpty());
    }
}
