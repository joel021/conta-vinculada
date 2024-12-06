package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.PessoaJuridica;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    public List<Contrato> searchByNomePessoaJuridica(String name, String unidade) {

        List<PessoaJuridica> companyList = pessoaJuridicaService.searchPessoaByName(name);
        List<Contrato> contratoList = new ArrayList<>();

        for (PessoaJuridica pessoaJuridica: companyList) {
            contratoList.addAll(contratoRepository.findAllByPessoaJuridicaAndUnidadeContaining(
                    Sort.by("fimVigencia").descending(),
                    pessoaJuridica,
                    unidade));
        }

        return contratoList;
    }

    public Contrato findById(Integer contratoId, String unidade) throws NotFoundException {

        if (contratoId != null) {
            Optional<Contrato> contratoOptional = contratoRepository.findByIdContratoAndUnidadeContaining(contratoId, unidade);

            if (contratoOptional.isPresent()) {
                return contratoOptional.get();
            }
        }

        throw new NotFoundException("Desculpe, o contrato com id " + contratoId + " não foi encontrado para a sua Seção Judiciária.");
    }

    public Contrato findByNumeroOrThrows(String numero, String unidade) throws NotFoundException {

        final List<Contrato> contratoOptional = contratoRepository.findByNumeroAndUnidadeContaining(numero, unidade);

        if (contratoOptional.isEmpty()) {
            throw new NotFoundException("Contrato com o número " + numero + " não foi encontrado para a sua Seção Judiciária.");
        }
        return contratoOptional.get(0);
    }

    public Contrato findContrato(Contrato contrato, String unidade) throws NotFoundException {

        if (contrato != null) {

            try {
                return findById(contrato.getIdContrato(), unidade);
            } catch (NotFoundException e) {

                return findByNumeroOrThrows(contrato.getNumero(), unidade);
            }
        }
        throw new NotFoundException("Não foi possível encontrar o contrato.");
    }

}
