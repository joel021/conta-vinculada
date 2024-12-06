package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.PessoaJuridicaRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaJuridicaService {


    private final PessoaJuridicaRepository pessoaJuridicaRepository;


    public PessoaJuridicaService(PessoaJuridicaRepository pessoaJuridicaRepository) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
    }

    public PessoaJuridica findByCnpj(String cnpj) throws NotFoundException {

        Optional<PessoaJuridica> pessoaJuridicaOptional = this.pessoaJuridicaRepository.findByCnpj(cnpj);

        if (pessoaJuridicaOptional.isPresent()) {
            return pessoaJuridicaOptional.get();
        }
        throw new NotFoundException("Não foi encontrado um registro de empresa com o CNPJ "+cnpj);
    }

    public List<PessoaJuridica> searchPessoaByName(String name) {

        return this.pessoaJuridicaRepository.findByNomeContains(Sort.by("nome").ascending(), name);
    }
}
