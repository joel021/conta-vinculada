package br.jus.trf1.sjba.contavinculada.utils;

import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.directory.api.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersFromJsonFile {

    public List<Usuario> getUsers(String prefix) throws NotFoundException {

        final Map<String, Object> objectMap = getUsersMap(prefix);
        if (objectMap.get("users") == null) {
            throw new NotFoundException("O arquivo de inicialização da base não foi encontrado ou não está corretamente formatado.");
        }
        List<Map<String, Object>> usuariosMap = (List<Map<String, Object>>) objectMap.get("users");
        return usuariosMap.stream().map(Usuario::getInstanceByMap).collect(Collectors.toList());
    }

    public Map<String, Object> getUsersMap(String prefix) throws NotFoundException {

        Map<String, Object> listUsersMaps;
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(getFileInputStream(prefix+"-users.json"), writer, Charset.defaultCharset());
            String usersString = writer.toString();
            listUsersMaps = new ObjectMapper().reader().readValue(usersString, Map.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new NotFoundException(e.getMessage());
        }
        return listUsersMaps;
    }

    public InputStream getFileInputStream(String fileName) {

        InputStream input = UsersFromJsonFile.class.getResourceAsStream(fileName);
        if (input == null) {
            input = UsersFromJsonFile.class.getClassLoader().getResourceAsStream(fileName);
        }
        return input;
    }
}
