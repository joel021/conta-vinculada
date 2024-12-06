package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.OficioMovimentacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.OficioMovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OficioMovimentacaoService {

    @Autowired
    private OficioMovimentacaoRepository oficioMovimentacaoRepository;

    public OficioMovimentacao findOrRegister(OficioMovimentacao oficioMovimentacao) {
        Optional<OficioMovimentacao> oficioMovimentacaoOptional = oficioMovimentacaoRepository.findById(oficioMovimentacao.getDocSei());
        return oficioMovimentacaoOptional.orElseGet(() -> oficioMovimentacaoRepository.save(oficioMovimentacao));
    }
}
