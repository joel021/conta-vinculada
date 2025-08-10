package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.NivelEnsino;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContratosTerceirizadosFromCsvTests {

    static ContratosTerceirizadosFromCsv contratosTerceirizadosFromCsv;
    static String[] line;

    @BeforeAll
    public static void setup() {

        contratosTerceirizadosFromCsv = new ContratosTerceirizadosFromCsv();
        line = "5886626,00899223-0001-32,ELETROCONTROLE LTDA,TARCÍSIO SANTOS DE OLIVEIRA,010982375-36,v,982375,MEC. DE REFRIGERAÇÃO,M,PARDO/A,SALVADOR,0, , ,,01/11/2023,SJBA,5442957000101,Seção Judiciária da Bahia".split(",");
    }

    @Test
    public void getFromFileOnResourcesContratosTest() throws NotAcceptableException, IOException {

        ContratosTerceirizadosData contratosTerceirizadosData = contratosTerceirizadosFromCsv.getFromFileOnResources();
        assertFalse(contratosTerceirizadosData.getContratoTerceirizadoList().isEmpty(), "Assert there are Funcionarios");
    }

    @Test
    public void getFromFileOnResourcesAfastamentosTest() throws NotAcceptableException, IOException {

        ContratosTerceirizadosData contratosTerceirizadosData = contratosTerceirizadosFromCsv.getFromFileOnResources();
        assertFalse(contratosTerceirizadosData.getAfastamentoList().isEmpty(), "Assert there are Afastamentos");
    }

    @Test
    public void instancianteFromLinesTest() throws NotAcceptableException {

        List<String> lines = new ArrayList<>();
        lines.add("Contrato nº:,CNPJ,Contratada,Nome do empregado,CPF,Verific.,CPF,Cargo Atividade,Nível Ensino,Cor/Raça,Lotação / Local de Exercício,Dias Ausentes,Nome do Empregado Substituto no Período de Ausência,CPF,CPF,Mês de Referência:,Unidade Gestora,CNPJ orgao publico,Nome orgao publico");
        lines.add("5886626,00899223-0001-32,ELETROCONTROLE LTDA,TARCÍSIO SANTOS DE OLIVEIRA,010982375-36,v,982375,MEC. DE REFRIGERAÇÃO,M,PARDO/A,SALVADOR,0, , ,,01/11/2023,SJBA,5442957000101,Seção Judiciária da Bahia");
        lines.add("5886626,00899223-0001-32,ELETROCONTROLE LTDA,GILVAN SANTOS FERREIRA,051058525-67,v,58525,AJUDANTE PRÁTICO,M,PRETO/A,SALVADOR,0, , ,,,SJBA,5442957000101,Seção Judiciária da Bahia");
        ContratosTerceirizadosData contratosTerceirizadosData = contratosTerceirizadosFromCsv.instantiateFromLines(lines);
        assertFalse(contratosTerceirizadosData.getContratoTerceirizadoList().isEmpty());
    }

    @Test
    public void getDataVigenciaYearTest() {

        Calendar dataVigenciaExpected = Calendar.getInstance();
        dataVigenciaExpected.set(Calendar.YEAR, 2023);

        var dataVigencia = contratosTerceirizadosFromCsv.getDataVigencia(line);
        assertEquals(dataVigenciaExpected.get(Calendar.YEAR), dataVigencia.getYear());
    }

    @Test
    public void getDataVigenciaMonthTest() {

        var dataVigencia = contratosTerceirizadosFromCsv.getDataVigencia(line);
        assertEquals(11, dataVigencia.getMonth().getValue());
    }

    @Test
    public void getNivelEnsinoTest() {

        var nivelEnsino = contratosTerceirizadosFromCsv.getNivelEnsino(line);
        assertEquals(NivelEnsino.M, nivelEnsino);
    }

    @Test
    public void getPessoaJuridicaCnpjTest() {

        var pessoaJuridica = contratosTerceirizadosFromCsv.getPessoaJuridica(line);
        assertEquals("00899223000132", pessoaJuridica.getCnpj());
    }

    @Test
    public void getPessoaJuridicaNomeTest() {

        var pessoaJuridica = contratosTerceirizadosFromCsv.getPessoaJuridica(line);
        assertEquals("ELETROCONTROLE LTDA", pessoaJuridica.getNome());
    }

    @Test
    public void getPessoaFisicaTest() {

        var pessoaFisica = contratosTerceirizadosFromCsv.getPessoaFisica(line);
        assertEquals("01098237536", pessoaFisica.getCpf());

    }

    @Test
    public void getPessoaFisicaNomeTest() {

        var pessoaFisica = contratosTerceirizadosFromCsv.getPessoaFisica(line);
        assertEquals("TARCÍSIO SANTOS DE OLIVEIRA", pessoaFisica.getNome());
    }

    @Test
    public void getContratoTest() {

        var contrato = contratosTerceirizadosFromCsv.getContrato(line);
        assertEquals("5886626", contrato.getNumero());
    }

    @Test
    public void getFuncionarioMatriculaTest() {

        var funcionario = contratosTerceirizadosFromCsv.getFuncionario(line);
        assertEquals("ba01098237536", funcionario.getMatricula());
    }

    @Test
    public void getFuncionarioRacaCorTest() {

        var funcionario = contratosTerceirizadosFromCsv.getFuncionario(line);
        assertEquals("PARDO/A", funcionario.getRacaCor());
    }

    @Test
    public void getLotacaoTest() {

        var lotacao = contratosTerceirizadosFromCsv.getLotacao(line);
        assertEquals("SALVADOR", lotacao.getDescricao());
    }

    @Test
    public void getContratoTerceirizadoCargoTest() {
        var contratoTerceirizado = contratosTerceirizadosFromCsv.getContratoTerceirizado(LocalDate.now(), line);
        assertEquals("MEC. DE REFRIGERAÇÃO", contratoTerceirizado.getCargo());
    }

    @Test
    public void getDiasAusentesTest() {

        int diasAusentes = contratosTerceirizadosFromCsv.getDiasAusentes(line);
        assertEquals(10, diasAusentes);
    }

    @Test
    public void getAfastamentoDayTest() {

        LocalDate dataFimExpected = LocalDate.now().plusDays(10);
        var afastamento = contratosTerceirizadosFromCsv.getAfastamento(LocalDate.now(), line);

        assertEquals(dataFimExpected.getDayOfMonth(), afastamento.getDataFim().getDayOfMonth());
    }

    @Test
    public void getAfastamentoMonthTest() {

        LocalDate dataFimExpected = LocalDate.now().plusDays(10);
        var afastamento = contratosTerceirizadosFromCsv.getAfastamento(LocalDate.now(), line);

        assertEquals(dataFimExpected.getMonth(), afastamento.getDataFim().getMonth());
    }

    @Test
    public void getAfastamentoCpfSubstitutoTest() {

        var afastamento = contratosTerceirizadosFromCsv.getAfastamento(LocalDate.now(), line);
        assertEquals("00294913556", afastamento.getCpfSubstituto());
    }

    @Test
    public void getNumeroContratoTest() {

        var numeroContrato = contratosTerceirizadosFromCsv.getNumeroContrato(line);
        assertEquals("5886626", numeroContrato);
    }

    @Test
    public void getNomeSubstitutoTest() {

        var nomeSubstituto = contratosTerceirizadosFromCsv.getNomeSubstituto(line);
        assertEquals("ANTONIO CARLOS DALTRO AZEVEDO", nomeSubstituto);
    }

    @Test
    public void getAfastamentoTest() {

        String[] line = "5886626,00899223-0001-32,ELETROCONTROLE LTDA,GILVAN SANTOS FERREIRA,051058525-67,v,58525,AJUDANTE PRÁTICO,M,PRETO/A,SALVADOR,0, , ,,,SJBA,5442957000101,Seção Judiciária da Bahia".split(",");
        AfastamentoData afastamentoData = contratosTerceirizadosFromCsv.getAfastamento(LocalDate.now(), line);
        assertNull(afastamentoData);
    }
}
