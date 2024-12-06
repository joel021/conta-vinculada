package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.dto.FuncionarioContratosDto;
import br.jus.trf1.sjba.contavinculada.core.dto.FuncionarioContratosMapper;
import br.jus.trf1.sjba.contavinculada.core.dto.UnidadeContratosFuncionariosDto;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.Funcionario;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoTerceirizadoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.jwtconf.AuthUserDatails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ContratoTerceirizadoService {

    @Autowired
    private ContratoTerceirizadoRepository contratoTerceirizadoRepository;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private LotacaoService lotacaoService;

    public ContratoTerceirizado register(ContratoTerceirizado contratoTerceirizado, String userUnidade) throws NotFoundException,
            NotAcceptableException {

        contratoTerceirizado.setDeletadoPor(null);
        contratoTerceirizado.setDeletadoEm(null);

        if (contratoTerceirizado.getId() != null) {
            return registerTermoAditivo(contratoTerceirizado);
        }
        return save(contratoTerceirizado, userUnidade);
    }

    public ContratoTerceirizado findById(Integer id) throws NotFoundException {

        if (id != null) {
            Optional<ContratoTerceirizado> contratoTerceirizadoOptional = contratoTerceirizadoRepository.findById(id);
            if (contratoTerceirizadoOptional.isPresent()) {
                return contratoTerceirizadoOptional.get();
            }
        }

        throw new NotFoundException("Não foi possível encontrar o contrato correspondente.");
    }

    public ContratoTerceirizado findLastByFuncionarioAndContrato(Integer funcionarioId, Integer contratoId) throws NotFoundException {

        final List<ContratoTerceirizado> contratoTerceirizadoList = findByFuncionarioAndContratoSortDataInicioDesc(funcionarioId, contratoId);

        if (contratoTerceirizadoList.isEmpty()) {
            throw new NotFoundException("O funcionário fornecido não foi encontrado no contrato.");
        }
        return contratoTerceirizadoList.get(0);
    }

    public List<ContratoTerceirizado> findByFuncionarioAndContratoSortDataInicioDesc(Integer funcionarioId, Integer contratoId) {

        if (funcionarioId == null || contratoId == null) {
            return new ArrayList<>();
        }

        Funcionario funcionario = Funcionario.builder().idFuncionario(funcionarioId).build();
        Contrato contrato = Contrato.builder().idContrato(contratoId).build();

        return contratoTerceirizadoRepository.findByFuncionarioAndContratoAndDeletadoEmOrderByDataInicioDesc(
                funcionario,
                contrato,
                null
        );
    }

    public ContratoTerceirizado findContratoTerceirizado(String funcionarioMatricula, String contratoNumero, String userUnidade)
            throws NotFoundException {

        final var funcionario = funcionarioService.findByMatriculaOrThrows(funcionarioMatricula);
        final var contrato = contratoService.findByNumeroOrThrows(contratoNumero, userUnidade);
        return findLastByFuncionarioAndContrato(funcionario.getIdFuncionario(), contrato.getIdContrato());
    }

    public ContratoTerceirizado saveIfNotExists(ContratoTerceirizado contratoTerceirizado, String userUnidade) throws NotFoundException,
            NotAcceptableException {

        try {
            return findContratoTerceirizado(
                    contratoTerceirizado.getFuncionario().getMatricula(),
                    contratoTerceirizado.getContrato().getNumero(),
                    userUnidade
            );
        } catch (NotFoundException ignored) {}
        return save(contratoTerceirizado, userUnidade);
    }

    public ContratoTerceirizado save(ContratoTerceirizado contratoTerceirizado, String userUnidade) throws NotFoundException,
            NotAcceptableException {

        contratoTerceirizado.setContrato(contratoService.findContrato(contratoTerceirizado.getContrato(), userUnidade));
        contratoTerceirizado.setFuncionario(funcionarioService.saveIfNotExists(contratoTerceirizado.getFuncionario()));
        contratoTerceirizado.setLotacao(lotacaoService.saveIfNotExists(contratoTerceirizado.getLotacao()));
       return checkUniqueSaveOrThrows(contratoTerceirizado);
    }

    public ContratoTerceirizado checkUniqueSaveOrThrows(ContratoTerceirizado contratoTerceirizado) throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoRepository
                .findByFuncionarioAndContratoAndDataInicioAndDeletadoEm(
                contratoTerceirizado.getFuncionario(),
                contratoTerceirizado.getContrato(),
                contratoTerceirizado.getDataInicio(),
                contratoTerceirizado.getDeletadoEm());

        if (contratoTerceirizadoList.isEmpty()) {
            return contratoTerceirizadoRepository.save(contratoTerceirizado);
        }
        throw new NotAcceptableException("Já existe uma associação desse profissional com a mesma data de início.");
    }

    public List<ContratoTerceirizado> findActiveByIdContrato(int idContrato) {

        return contratoTerceirizadoRepository.findActiveByIdContratoAndBeforeDateOrderByDataInicioDesc(idContrato);
    }

    public List<ContratoTerceirizado> findNonDeletedByIdContrato(int idContrato) {

        Contrato contrato = new Contrato();
        contrato.setIdContrato(idContrato);
        return contratoTerceirizadoRepository.findByContratoAndDeletadoEmOrderByDataInicioDesc(
                contrato,
                null);
    }

    public List<FuncionarioContratosDto> funcionarioContratosDtoListByIdContrato(Integer idContrato) {


        final List<ContratoTerceirizado> contratoTerceirizadoPage = findNonDeletedByIdContrato(idContrato);
        Collection<FuncionarioContratosDto> funcionarioContratosDtoList = new FuncionarioContratosMapper(contratoTerceirizadoPage)
                .funcionarioContratosDtoList();

        return funcionarioContratosDtoList.stream().toList();
    }

    public UnidadeContratosFuncionariosDto funcionarionsByUnidade(String unidade) {

        return UnidadeContratosFuncionariosDto
                .fromContratoTerceirizadoList(
                        contratoTerceirizadoRepository.findNotDeletedActiveByUnidadeOrderByInicioVigenciaDataInicioDesc(unidade)
                );
    }

    public void setDataDesligamento(int funcionarioId, int contratoId, LocalDate dataDesligamento) {

        final List<ContratoTerceirizado> contratoEmployeeAssociations = findByFuncionarioAndContratoSortDataInicioDesc(
                funcionarioId,
                contratoId);
        for(ContratoTerceirizado contratoTerceirizado: contratoEmployeeAssociations){
            contratoTerceirizado.setDataDesligamento(dataDesligamento);
        }

        contratoTerceirizadoRepository.saveAll(contratoEmployeeAssociations);
    }

    public ContratoTerceirizado registerTermoAditivo(ContratoTerceirizado contratoTerceirizado) throws NotFoundException,
            NotAcceptableException {

        final ContratoTerceirizado contratoTerceirizadoFound = findById(contratoTerceirizado.getId());
        contratoTerceirizado.setFuncionario(contratoTerceirizadoFound.getFuncionario());
        contratoTerceirizado.setContrato(contratoTerceirizadoFound.getContrato());
        contratoTerceirizado.setLotacao(lotacaoService.saveIfNotExists(contratoTerceirizado.getLotacao()));
        contratoTerceirizado.setDeletadoPor(null);
        contratoTerceirizado.setDeletadoEm(null);
        final ContratoTerceirizado termoAditivoSaved = checkUniqueSaveOrThrows(contratoTerceirizado);

        setDataDesligamento(
                contratoTerceirizadoFound.getFuncionario().getIdFuncionario(),
                contratoTerceirizadoFound.getContrato().getIdContrato(),
                null
                );
        return termoAditivoSaved;
    }

    public void delete(Integer id, AuthUserDatails userDetails) throws NotFoundException {

        ContratoTerceirizado contratoTerceirizado = findById(id);
        contratoTerceirizado.setDeletadoPor(userDetails);
        contratoTerceirizado.setDeletadoEm(Calendar.getInstance());
        contratoTerceirizadoRepository.save(contratoTerceirizado);
    }

}