package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.SecaoJudiciaria;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.SecaoJudiciariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecaoJudiciariaService {

    @Autowired
    private SecaoJudiciariaRepository secaoJudiciariaRepository;

    public List<SecaoJudiciaria> findBySigla(String sigla) {

        String siglaUnidade = sigla != null ? sigla.trim().toUpperCase() : null;
        return secaoJudiciariaRepository.findBySigla(siglaUnidade);
    }

    public SecaoJudiciaria findOneBySiglaOrNull(String sigla) {

        List<SecaoJudiciaria> secaoJudiciariaList = findBySigla(sigla);

        if (secaoJudiciariaList.isEmpty()) {
            return null;
        }
        return secaoJudiciariaList.get(0);
    }

    public SecaoJudiciaria findOrCreate(SecaoJudiciaria secaoJudiciaria, String dominio) {

        final String sigla = secaoJudiciaria.getSigla() != null ? secaoJudiciaria.getSigla() : "";

        List<SecaoJudiciaria> secaoJudiciariaList = findBySigla(sigla);
        if (secaoJudiciariaList.isEmpty()) {
            secaoJudiciaria.setSiglaByDominio(dominio);
            return secaoJudiciariaRepository.save(secaoJudiciaria);
        }
        return secaoJudiciariaList.get(0);
    }
}
