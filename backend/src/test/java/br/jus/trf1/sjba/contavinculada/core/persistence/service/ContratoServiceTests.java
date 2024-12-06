package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.Contrato;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.ContratoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContratoServiceTests {

    @Autowired
    private ContratoService contratoService;

    @MockBean
    private ContratoRepository contratoRepository;

    private Contrato contrato;

    String unidade = "SJBA";

    @BeforeEach
    public void setup() {

        contrato = Contrato.builder()
                .idContrato(1)
                .numero("0909090923")
                .build();
        when(contratoRepository.save(contrato)).thenReturn(contrato);
        when(contratoRepository.findByNumeroAndUnidadeContaining(contrato.getNumero(), unidade)).thenReturn(List.of(contrato));
        when(contratoRepository.findByIdContratoAndUnidadeContaining(contrato.getIdContrato(), "SJBA")).thenReturn(Optional.of(contrato));
    }

    @Test
    public void findContratoTest() throws NotFoundException {

        Contrato contratoWithId = Contrato.builder()
                .idContrato(contrato.getIdContrato())
                .build();

        Contrato contratoFound = contratoService.findContrato(contratoWithId, unidade);
        assertEquals(contratoFound.getNumero(), contrato.getNumero());
    }

}
