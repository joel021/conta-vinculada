package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.utils.StringUtils;
import lombok.Getter;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static br.jus.trf1.sjba.contavinculada.utils.DateUtils.fromCalendar;

@Getter
public class ContratosTerceirizadosFromCsv {

    private final String CSV_FILE = "setup_terceirizados.csv";
    private final StringUtils stringUtils = new StringUtils();

    public ContratosTerceirizadosData getFromFileOnResources() throws IOException, NotAcceptableException {

        CsvReader csvReader = new CsvReader();
        return instantiateFromLines(csvReader.readCsvFileAsLinesFromResources(CSV_FILE));
    }

    public ContratosTerceirizadosData instantiateFromLines(List<String> lines) throws NotAcceptableException {

        if (lines.size() < 2) {
            throw new NotAcceptableException("O csv fornecido não possui os dados esperados.");
        }

        List<ContratoTerceirizado> contratoTerceirizadoList = new ArrayList<>();
        List<AfastamentoData> afastamentoList = new ArrayList<>();

        final LocalDate dataVigencia = getDataVigencia(lines.get(1).split(","));

        for (int i = 1; i < lines.size(); i++) {

            String[] line = lines.get(i).split(",");
            contratoTerceirizadoList.add(getContratoTerceirizado(dataVigencia, line));

            final var afastamento = getAfastamento(dataVigencia, line);
            if (afastamento != null){
                afastamentoList.add(afastamento);
            }
        }

        return new ContratosTerceirizadosData(contratoTerceirizadoList, afastamentoList);
    }

    public LocalDate getDataVigencia(String[] line) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            cal.setTime(sdf.parse(line[15]));
            return fromCalendar(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fromCalendar(cal);
    }

    public String getNomeSubstituto(String[] line) {

        return stringUtils.cleanString(stringUtils.contentOrNull(line[12]));
    }

    public AfastamentoData getAfastamento(LocalDate dataVigencia, String[] line) {

        final int diasAusentes = getDiasAusentes(line);

        if (diasAusentes > 0) {

            AfastamentoData afastamento = new AfastamentoData();
            afastamento.setNumeroContrato(getNumeroContrato(line));
            afastamento.setCpfSubstituido(getCpfFuncionario(line));
            afastamento.setCpfSubstituto(stringUtils.onlyNumbersOrNull(line[13]));
            afastamento.setNomeSubstituto(getNomeSubstituto(line));
            afastamento.setDataInicio(dataVigencia);

            LocalDate dataFim = dataVigencia.plusDays(getDiasAusentes(line));
            afastamento.setDataFim(dataFim);
            return afastamento;
        }
        return null;
    }

    public int getDiasAusentes(String[] line) {

        if (line.length > 11){
            return stringUtils.integerOrDefault(line[11], 0);
        }
        return 0;
    }

    public ContratoTerceirizado getContratoTerceirizado(LocalDate dataInicio, String[] line) {

        ContratoTerceirizado contratoTerceirizado = new ContratoTerceirizado();
        contratoTerceirizado.setFuncionario(getFuncionario(line));
        contratoTerceirizado.setCargo(line[7]);
        contratoTerceirizado.setLotacao(getLotacao(line));
        contratoTerceirizado.setContrato(getContrato(line));
        contratoTerceirizado.setDataInicio(dataInicio);

        return contratoTerceirizado;
    }

    public Lotacao getLotacao(String[] line) {

        Lotacao lotacao = new Lotacao();
        lotacao.setDescricao(line[10]);
        return lotacao;
    }

    public Funcionario getFuncionario(String[] line) {

        Funcionario funcionario = new Funcionario();
        funcionario.setPessoaFisica(getPessoaFisica(line));
        funcionario.setMatricula("ba"+getCpfFuncionario(line));
        funcionario.setOficialJustica(false);
        funcionario.setNivel(getNivelEnsino(line));
        funcionario.setRacaCor(stringUtils.cleanString(line[9]));

        return funcionario;
    }

    public String getCpfFuncionario(String[] line) {

        return stringUtils.onlyNumbersOrNull(line[4]);
    }

    public String getNumeroContrato(String[] line) {

        return stringUtils.onlyNumbersOrNull(line[0]);
    }

    public Contrato getContrato(String[] line) {

        Contrato contrato = new Contrato();
        contrato.setNumero(getNumeroContrato(line));
        contrato.setPessoaJuridica(getPessoaJuridica(line));
        contrato.setUnidade(getUnidade(line));
        contrato.setSecaoJudiciaria(getSecaoJudiciaria(line));

        return contrato;
    }

    public SecaoJudiciaria getSecaoJudiciaria(String[] line) {
        SecaoJudiciaria secaoJudiciaria = new SecaoJudiciaria();
        secaoJudiciaria.setCnpjSecao(getCNPJUnidadeGestora(line));
        secaoJudiciaria.setSigla(getUnidade(line));
        secaoJudiciaria.setNome(stringUtils.contentOrNull(line[18]));
        return secaoJudiciaria;
    }

    public String getUnidade(String[] line) {
        return stringUtils.contentOrNull(line[16]);
    }

    public PessoaFisica getPessoaFisica(String[] line) {

        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setCpf(getCpfFuncionario(line));
        pessoaFisica.setNome(stringUtils.cleanString(line[3]));
        return pessoaFisica;
    }

    public PessoaJuridica getPessoaJuridica(String[] line) {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj(stringUtils.onlyNumbersOrNull(line[1]));
        pessoaJuridica.setNome(stringUtils.cleanString(line[2]));
        return pessoaJuridica;
    }

    public NivelEnsino getNivelEnsino(String[] line) {

        if (StringUtils.isEmpty(line[8])) {
            return NivelEnsino.I;
        } else {
            return NivelEnsino.valueOf(line[8].trim().toUpperCase());
        }
    }

    public String getCNPJUnidadeGestora(String[] line) {
        return line[17].trim();
    }
}
