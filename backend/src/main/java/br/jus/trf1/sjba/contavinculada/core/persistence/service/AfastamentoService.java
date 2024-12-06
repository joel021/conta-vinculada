package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Afastamento;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.AfastamentoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AfastamentoService {

    @Autowired
    private AfastamentoRepository afastamentoRepository;

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ContratoService contratoService;

    public Afastamento saveIfNotExists(Afastamento afastamento, String userUnidade) throws NotFoundException, NotAcceptableException {

        final ContratoTerceirizado substituidoContratoTerceirizado = contratoTerceirizadoService.findContratoTerceirizado(
                afastamento.getSubstituido().getFuncionario().getMatricula(),
                afastamento.getSubstituido().getContrato().getNumero(),
                userUnidade
                );

        afastamento.setSubstituido(substituidoContratoTerceirizado);
        final var afastamentoList = findBySubstituidoAndDataInicio(
                substituidoContratoTerceirizado.getId(),
                afastamento.getDataInicio()
        );
        if (afastamentoList.isEmpty()) {
            return save(afastamento, userUnidade);
        }
        return afastamentoList.get(0);
    }

    public Afastamento save(Afastamento afastamento, String userUnidade) throws NotFoundException, NotAcceptableException {

        afastamento.setSubstituto(contratoTerceirizadoService.saveIfNotExists(afastamento.getSubstituto(), userUnidade));
        return afastamentoRepository.save(afastamento);
    }

    public List<Afastamento> findBySubstituidoAndDataInicio(Integer contratoTerceirizadoId, LocalDate dataInicio) {

        final var contratoTerceirizado = ContratoTerceirizado.builder().id(contratoTerceirizadoId).build();
        return afastamentoRepository.findBySubstituidoAndDataInicio(
                contratoTerceirizado,
                dataInicio);
    }
}
