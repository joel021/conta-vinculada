package br.jus.trf1.sjba.contavinculada.security.service;


import br.jus.trf1.sjba.contavinculada.core.persistence.service.UnidadeService;
import br.jus.trf1.sjba.contavinculada.exception.*;
import br.jus.trf1.sjba.contavinculada.notifier.EmailService;
import br.jus.trf1.sjba.contavinculada.security.dto.*;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.JwtService;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.security.model.Papel;
import br.jus.trf1.sjba.contavinculada.security.repository.UserRepository;
import br.jus.trf1.sjba.contavinculada.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtTokenProvider;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UnidadeService unidadeService;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserAuthenticatedDTO signinWithLdap(UserAuthDTO userAuthDTO) throws AuthenticationException, NotAuthorizedYetException {

        if (authenticateWithLdap(userAuthDTO)) {
            Usuario usuario = findOrRegisterUser(userAuthDTO.getUsuario(), userAuthDTO.getDominio());
            if (!usuario.isEnabled()){
                throw new NotAuthorizedYetException("Você não está autorizado para acessar este sistema. Contate um dos adminstradores.");
            }
            return allowAccess(usuario);
        } else {
            throw new AuthenticationException("Seu usuário ou senha da Justiça estão incorretos. O usuário deve ser a sua matrícula.");
        }

    }

    public UserAuthenticatedDTO allowAccess(Usuario usuario) {

        UserAuthenticatedDTO userAuthenticatedDTO = UserAuthenticatedDTO.getInstanceFrom(usuario);
        userAuthenticatedDTO.setToken(jwtTokenProvider.generateToken(AuthUserDatails.instanceFrom(usuario)));
        userAuthenticatedDTO.setUnidade(usuario.getUnidade());

        return userAuthenticatedDTO;
    }

    public Usuario findOrRegisterUser(String userName, String dominio){

        Optional<Usuario> appUser = userRepository.findByUsuario(userName);

        if (appUser.isEmpty()) {
            Usuario usuarioToRegister = new Usuario();
            usuarioToRegister.setUsuario(userName);
            usuarioToRegister.getPapeis().add(Papel.ROLE_GUEST);
            usuarioToRegister.setDominio(dominio);
            usuarioToRegister.setEnabled(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    notifyAboutNewUser(usuarioToRegister.getUsuario());
                }
            }).start();

            return userRepository.save(usuarioToRegister);
        }
        return appUser.get();
    }

    public UserResponseDTO updateUsuario(Usuario usuario) throws UsernameNotFoundException {

        Usuario userFound = findByUsuario(usuario.getUsuario());
        userFound.setNome(usuario.getNome());
        userFound.setEmail(usuario.getEmail());

        if (usuario.getUnidade() != null) {
            userFound.setUnidade(unidadeService.findOrCreate(usuario.getUnidade(), usuario.getDominio()));
        }

        return allowAccess(userRepository.save(userFound));
    }

    public UserResponseDTO updateUsuario(String usuario, UpdateUserDto usuarioUsuarioDto) {

        Usuario usuarioUsuario = Usuario.getInstanceByUpdateDto(usuarioUsuarioDto);
        usuarioUsuario.setUsuario(usuario);
        return updateUsuario(usuarioUsuario);
    }

    public UserResponseDTO updateWithLdapVerification(UserDataDTO usuarioDto) throws AuthenticationException {

        if (authenticateWithLdap(usuarioDto)) {
            return updateUsuario(Usuario.getInstanceByDataDto(usuarioDto));
        } else {
            throw new AuthenticationException("Seu login ou senha estão incorretos, por favor, tente novamente ou " +
                    "volte para a tela de login.");
        }
    }

    public Usuario findByUsuario(String username) throws UsernameNotFoundException {
        Optional<Usuario> appUser = userRepository.findByUsuario(username);
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("Este usuário não foi encontrado.");
        }
        return appUser.get();
    }

    public String refresh(String username) throws NotFoundException {

        Optional<Usuario> optionalAppUser = userRepository.findByUsuario(username);
        if (optionalAppUser.isPresent()){
            return jwtTokenProvider.generateToken(AuthUserDatails.instanceFrom(optionalAppUser.get()));
        }
        throw new NotFoundException("O usuário não foi encontrado.");
    }

    public Usuario registerFromDataDto(UserDataDTO userDataDTO) {
        return register(Usuario.getInstanceByDataDto(userDataDTO));
    }

    public Usuario register(Usuario usuario) {

        if (usuario.getUnidade() != null){
            usuario.setUnidade(unidadeService.findBySiglaUnidadeOrCreate(usuario.getUnidade().getSiglaUnidade()));
        }

        return save(usuario);
    }

    public Usuario save(Usuario usuario) {

        Optional<Usuario> usuarioExistent = userRepository.findByUsuario(usuario.getUsuario());
        usuarioExistent.ifPresent(value -> usuario.setIdUsuario(value.getIdUsuario()));
        return userRepository.save(usuario);
    }

    public List<Usuario> findAll() {
        return userRepository.findAll(Sort.by("nome").ascending());
    }

    public Usuario updateAuthorization(Usuario usuario) throws NotAllowedException {

        Optional<Usuario> appUserFoundOptional = userRepository.findByUsuario(usuario.getUsuario());

        if(appUserFoundOptional.isPresent()) {

            Usuario usuarioFound = appUserFoundOptional.get();
            long count = userRepository.countByPapeisContains(Papel.ROLE_ADMIN);

            if (usuarioFound.getPapeis().contains(Papel.ROLE_ADMIN) && !usuario.getPapeis().contains(Papel.ROLE_ADMIN) && count <= 1){
                throw new NotAllowedException("Não é permitido mudar o papel ou desativar esse usuário administrador " +
                        " porque o sistema tem que ter pelo menos um administrador ativo.");
            }
            usuarioFound.setPapeis(usuario.getPapeis());
            usuarioFound.setEnabled(usuario.isEnabled());
            return userRepository.save(usuarioFound);
        } else {
            throw new NotAllowedException("Desculpe, houve um erro interno de forma que o sistema não consiguiu recuperar " +
                    "as informações do usuário requisitado para mudança. Por favor, atualize a página.");
        }
    }

    public Set<String> findEmailsOfAllAdmins() {
        Set<String> adminsEmails = userRepository.findByPapeisContains(Papel.ROLE_ADMIN)
                .stream().map(Usuario::getEmail).collect(Collectors.toSet());
        adminsEmails.remove(null);
        return adminsEmails;
    }


    public void notifyAboutNewUser(String username) {

        Set<String> operationalEmails = findEmailsOfAllAdmins();
        try {
            emailService.sendHTMLEmail(operationalEmails, "Novo usuário no sistema", "<p>Olá</p><p>O usuário " +
                    "<b>"+username.substring(5)+"</b> se registrou no sistema pela primeira vez, caso ele não seja um dos usuários pré registrados," +
                    " ele não terá acesso ao sistema. Caso queira decidir se ele pode ou não ter acesso, por favor, entre no sistema, e acesse " +
                    "a tela de autorização de usuários.");
        } catch (Exception ex) {
            LOGGER.warning("Try to send emails in order to advice an error, but was not possible. Error message:\n"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean authenticateWithLdap(UserAuthDTO userAuthDTO) throws AuthenticationException {

        if (StringUtils.isEmpty(userAuthDTO.getDominio())) {
            return ldapService.authenticate(userAuthDTO.getUsuario(), userAuthDTO.getSenha());
        }

        return ldapService.authenticate(userAuthDTO.getDominio().toLowerCase()+"\\"+userAuthDTO.getUsuario(), userAuthDTO.getSenha());
    }

    public void deleteByUsuario(String usuario) {

        Optional<Usuario> usuarioFound = userRepository.findByUsuario(usuario);
        usuarioFound.ifPresent(value -> userRepository.deleteById(value.getIdUsuario()));
    }
}
