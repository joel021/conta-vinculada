package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaFisica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaFisicaRepository;
import br.jus.trf1.sjba.contavinculada.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaFisicaService {

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    public PessoaFisica saveIfNotExists(PessoaFisica pessoaFisica) {

        Optional<PessoaFisica> pessoaFisicaOptional = pessoaFisicaRepository.findByCpf(pessoaFisica.getCpf());
        return pessoaFisicaOptional.orElseGet(() -> pessoaFisicaRepository.save(pessoaFisica));
    }

    public PessoaFisica update(PessoaFisica pessoaFisica) {

        if (pessoaFisica == null) {
            return null;
        }

        PessoaFisica pessoaFisicaFound = findById(pessoaFisica.getIdPessoa() != null ? pessoaFisica.getIdPessoa() : 0);
        if (pessoaFisicaFound == null) {
            return null;
        }
        pessoaFisicaFound.setCpf(pessoaFisica.getCpf());
        pessoaFisicaFound.setNome(pessoaFisica.getNome());

        return pessoaFisicaRepository.save(pessoaFisicaFound);
    }

    public PessoaFisica findById(Integer idPessoa) {

        Optional<PessoaFisica> pessoaFisicaOptional = pessoaFisicaRepository.findById(idPessoa);
        return pessoaFisicaOptional.orElse(null);
    }

    public List<PessoaFisica> findByNomeContaining(String nome) {

        if (StringUtils.isEmpty(nome)) {
            return new ArrayList<>();
        }
        return pessoaFisicaRepository.findByNomeContainsLimited(nome);
    }
}
