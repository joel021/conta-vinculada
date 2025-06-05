package br.jus.trf1.sjba.contavinculada.security.controller;

import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraint;
import br.jus.trf1.sjba.contavinculada.core.validation.FieldConstraintEnum;
import br.jus.trf1.sjba.contavinculada.core.validation.Validator;
import br.jus.trf1.sjba.contavinculada.exception.*;
import br.jus.trf1.sjba.contavinculada.security.dto.*;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO search(@PathVariable String username) {
        return modelMapper.map(userService.findByUsuario(username), UserResponseDTO.class);
    }

    @GetMapping("/refresh")
    public String refresh(HttpServletRequest req) throws NotFoundException {
        return userService.refresh(req.getRemoteUser());
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Usuario>> listUsers(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @PatchMapping("/authorization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Usuario> updateAuthorization (@RequestBody Usuario usuario) throws NotAllowedException {

        return ResponseEntity.ok().body(userService.updateAuthorization(usuario));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authWithLdap(@Valid @RequestBody UserAuthDTO userDataDTO,
                                                         BindingResult result) throws AuthenticationException {

        Map<String, Object> response = new HashMap<>();
        response.put("errors", new ArrayList<>());

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> ((List<String>) response.get("errors") ).add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            UserAuthenticatedDTO userAuthenticated = userService.signinWithLdap(userDataDTO);
            response.put("userDetails", userAuthenticated);
            return ResponseEntity.ok().body(response);
        } catch (NotAuthorizedYetException e) {

            UserAuthenticatedDTO userDetails = new UserAuthenticatedDTO();
            userDetails.setUsuario(userDataDTO.getUsuario());
            userDetails.setToken("");
            userDetails.setPapels(List.of(Papel.ROLE_GUEST));
            response.put("userDetails", userDetails);
            return ResponseEntity.ok().body(response);
        }
    }

    @PatchMapping("/")
    public ResponseEntity<?> updateWhenAlreadyAuthenticated(@Valid @RequestBody UpdateUserDto userDataDTO,
                                                            BindingResult result) throws NotAcceptableException {

        Map<String, Object> response = new HashMap<>();
        response.put("errors", new ArrayList<>());

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> ((List<String>) response.get("errors") ).add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Validator validator = Validator.builder()
                .addConstraint("unidade", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "A unidade deve ser fornecida."))
                .addConstraint("siglaUnidade", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "A sigla da unidade deve ser fornecida."))
                .addConstraint("secaoJudiciaria", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "A sigla da unidade deve ser fornecida."))
                .addConstraint("nome", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "O nome da Seção Judiciária deve ser fornceido."))
                .addConstraint("cnpjSecao", new FieldConstraint(FieldConstraintEnum.NOT_NULL,
                        "O cnpj deve ser fornecido."));
        validator.validateOrThrows(userDataDTO);

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponseDTO userDetails = userService.updateUsuario(user.getUsername(), userDataDTO);

        response.put("userDetails", userDetails);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/update/{username}")
    public ResponseEntity<?> updateNonAuthenticated(@PathVariable String username,
                                                    @Valid @RequestBody UserDataDTO userDataDTO, BindingResult result)
            throws AuthenticationException {

        Map<String, Object> response = new HashMap<>();
        response.put("errors", new ArrayList<>());

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> ((List<String>) response.get("errors") ).add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        UserResponseDTO userDetails = userService.updateWithLdapVerification(userDataDTO);
        response.put("userDetails", userDetails);
        return ResponseEntity.ok().body(response);
    }

}
