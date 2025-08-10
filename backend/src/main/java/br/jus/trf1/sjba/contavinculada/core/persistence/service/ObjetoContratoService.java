package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ObjetoContrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ObjetoContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjetoContratoService {


    @Autowired
    private ObjetoContratoRepository objetoContratoRepository;

    public ObjetoContrato saveIfNotExists(ObjetoContrato objetoContrato) {

        if (objetoContrato.idObjetoContrato == null) {
            Optional<ObjetoContrato> objContratoOpt = objetoContratoRepository.findById(objetoContrato.getIdObjetoContrato());

            if (objContratoOpt.isPresent()) {
                return objContratoOpt.get();
            }
        }

        return save(objetoContrato);
    }

    public ObjetoContrato save(ObjetoContrato objetoContrato){

        return objetoContratoRepository.save(objetoContrato);
    }
}
