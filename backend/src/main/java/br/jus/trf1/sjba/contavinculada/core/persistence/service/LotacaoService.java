package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Lotacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.LotacaoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LotacaoService {

    @Autowired
    private LotacaoRepository lotacaoRepository;

    public Lotacao saveIfNotExists(Lotacao lotacao) {

        if (lotacao == null) {
            return null;
        }

        try {
            return findById(lotacao.getIdLotacao());
        } catch (NotFoundException e) {

            List<Lotacao> lotacaoList = lotacaoRepository.findByDescricao(lotacao.getDescricao());
            if (lotacaoList.isEmpty()) {
                return lotacaoRepository.save(lotacao);
            }
            return lotacaoList.get(0);
        }

    }

    public Lotacao findById(Integer lotacaoId) throws NotFoundException {

        if (lotacaoId != null) {
            Optional<Lotacao> lotacaoOptional = lotacaoRepository.findById(lotacaoId);

            if (lotacaoOptional.isPresent()) {
                return lotacaoOptional.get();
            }
        }
        throw new NotFoundException("A Lotação com id "+lotacaoId+" não foi encontrada.");
    }
}
