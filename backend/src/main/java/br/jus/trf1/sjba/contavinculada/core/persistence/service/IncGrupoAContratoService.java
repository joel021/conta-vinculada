package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.IncGrupoAContratoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncGrupoAContratoService {

    @Autowired
    private IncGrupoAContratoRepository incGrupoAContratoRepository;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private UserService userService;

    public IncGrupoAContrato save(IncGrupoAContrato incGrupoAContrato, String userUnidade) throws NotFoundException, NotAcceptableException {

        if (incGrupoAContrato.getContrato().getIdContrato() == null) {
            throw new NotAcceptableException("Você deve informar o contrato.");
        }

        Contrato contrato = contratoService.findById(incGrupoAContrato.getContrato().getIdContrato(), userUnidade);
        incGrupoAContrato.setContrato(contrato);
        incGrupoAContrato.setData(incGrupoAContrato.getData() == null ? LocalDate.now() : incGrupoAContrato.getData());

        return incGrupoAContratoRepository.save(incGrupoAContrato);
    }

    public List<IncGrupoAContrato> findByIdContrato(Integer idContrato) {

        Contrato contrato = new Contrato();
        contrato.setIdContrato(idContrato);
        return incGrupoAContratoRepository.findByContratoAndDeletadoEmOrderByDataDesc(contrato, null);
    }

}
