package br.jus.trf1.sjba.contavinculada.core.persistence.repository;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ContratoRepositoryTests {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    private List<PessoaJuridica> pessoaJuridicaList;

    private Contrato contrato;

    @BeforeEach
    public void setup() {

        int personQtd = 3;
        int qtdContractPerPerson = 2;

        pessoaJuridicaList = new ArrayList<>();

        for(int i = 0; i < personQtd; i++){

            PessoaJuridica pessoaJuridica = new PessoaJuridica("283000000000"+i);
            pessoaJuridica.setNome("Company "+i);
            pessoaJuridicaRepository.save(pessoaJuridica);

            for(int j = 0; j < qtdContractPerPerson; j++){
                Contrato contrato = new Contrato();
                contrato.setPessoaJuridica(pessoaJuridica);
                contrato.setUnidade("SJBA e JFFFF");
                contratoRepository.save(contrato);
            }

            pessoaJuridicaList.add(pessoaJuridica);
        }

        LocalDate today = LocalDate.now();
        contrato = new Contrato(1, "SJBA", null,null, today,
                today, today,"893", null);
        contratoRepository.save(contrato);
    }

    @Test
    public void findTest() {
        assertNotNull(contratoRepository.findById(1).get());
    }

    @Test
    public void findAllByPessoaJuridicaTest() {

        List<Contrato> contratoList = contratoRepository.findAllByPessoaJuridicaAndUnidadeContaining(
                Sort.by("fimVigencia").descending(), pessoaJuridicaList.get(0), "SJBA");

        assertFalse(contratoList.isEmpty());
    }

    @Test
    public void findAllByPessoaJuridicaWhenPessoaNullTest() {

        List<Contrato> contratoList = contratoRepository.findAllByPessoaJuridicaAndUnidadeContaining(
                Sort.by("fimVigencia").descending(), null, "SJBA");

        assertNotNull(contratoList);
    }

    @Test
    public void findByNumeroTest() {

        List<Contrato> contratoFound = contratoRepository.findByNumeroAndUnidadeContaining(contrato.getNumero(), "SJBA");
        assertFalse(contratoFound.isEmpty());
    }

}
