package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PessoaFisicaTest {

    @Test
    void testSetCpfPadsZerosToLeft() {
        PessoaFisica pessoa = new PessoaFisica();
        String shortCpf = "123-45-6789wfewfa"; // 9 digits

        pessoa.setCpf(shortCpf);
        assertEquals("00123456789", pessoa.getCpf(),
                "it should be left-padded with zeros to 11 digits");
    }

    @Test
    void testSetCpfRemovesNonDigitsAndPads() {
        // Given: a CPF with non-digit characters
        PessoaFisica pessoa = new PessoaFisica();
        String cpfWithDots = "123.456-789"; // 9 digits with formatting

        pessoa.setCpf(cpfWithDots);

        assertEquals("00123456789", pessoa.getCpf());
    }

    @Test
    void testSetCpfKeeps11DigitCpf() {
        PessoaFisica pessoa = new PessoaFisica();
        String validCpf = "12345678901";

        pessoa.setCpf(validCpf);

        assertEquals("12345678901", pessoa.getCpf());
    }

    @Test
    void testSetCpfEmpty() {
        PessoaFisica pessoa = new PessoaFisica();
        String validCpf = "wgyvvytuyuyvtfewfef";

        pessoa.setCpf(validCpf);

        assertEquals("00000000000", pessoa.getCpf());
    }

    @Test
    void testSetCpfNull() {
        PessoaFisica pessoa = new PessoaFisica();
        String validCpf = null;

        pessoa.setCpf(validCpf);

        assertEquals("00000000000", pessoa.getCpf());
    }

}