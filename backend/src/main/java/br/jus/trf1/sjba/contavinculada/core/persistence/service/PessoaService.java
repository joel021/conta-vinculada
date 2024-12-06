package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Pessoa;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Pessoa> searchPessoaByName(String name) {
        return pessoaRepository.findByNomeContains(Sort.by("nome").ascending(), name);
    }
}
