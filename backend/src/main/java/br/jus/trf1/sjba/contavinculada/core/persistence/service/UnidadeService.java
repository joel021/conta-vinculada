package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Unidade;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.UnidadeRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private SecaoJudiciariaService secaoJudiciariaService;

    public Unidade findByIdOrNull(Integer id) {

        if (id != null) {
            return unidadeRepository.findById(id).orElse(null);
        } else {
            return null;
        }
    }

    public Unidade findOrCreate(Unidade unidade, String dominio) {

        List<Unidade> unidadeList = unidadeRepository.findBySiglaUnidadeAndSecaoJudiciaria(unidade.getSiglaUnidade(), unidade.getSecaoJudiciaria());
        if (unidadeList.isEmpty()) {

            if (unidade.getSecaoJudiciaria() != null) {
                unidade.setSecaoJudiciaria(secaoJudiciariaService.findOrCreate(unidade.getSecaoJudiciaria(), dominio));
            }
            return unidadeRepository.save(unidade);
        }
        return unidadeList.get(0);
    }

    public Unidade findBySiglaUnidadeOrCreate(String siglaUnidade) {

        List<Unidade> unidadeList = findBySiglaUnidade(siglaUnidade);

        if (unidadeList.isEmpty()) {

            String siglaUnidadeFormatted = siglaUnidade != null ? siglaUnidade.trim().toUpperCase() : null;
            Unidade unidade = new Unidade(null, "Não Definida", siglaUnidadeFormatted,
                    null, secaoJudiciariaService.findOneBySiglaOrNull(siglaUnidade));
            return unidadeRepository.save(unidade);
        }
        return unidadeList.get(0);
    }

    public List<Unidade> findBySiglaUnidade(String siglaUnidade) {

        String unidade = siglaUnidade != null ? siglaUnidade.toUpperCase().trim() : null;

        return unidadeRepository.findBySiglaUnidade(unidade);
    }

}
