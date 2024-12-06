package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;

public class AfastamentoBuilder {

    private final AfastamentoData afastamentoData;

    public AfastamentoBuilder(AfastamentoData afastamentoData) {
        this.afastamentoData = afastamentoData;
    }

    public Afastamento buildAfastamento() {

        return Afastamento.builder()
                .dataInicio(afastamentoData.getDataInicio())
                .dataFim(afastamentoData.getDataFim())
                .substituido(buildSubstituido())
                .substituto(buildSubstituto())
                .build();
    }

    public ContratoTerceirizado buildSubstituido() {

        final var contrato = Contrato.builder()
                .numero(afastamentoData.getNumeroContrato())
                .build();

        final var pessoaFisicaSubstituido = PessoaFisica.builder()
                .cpf(afastamentoData.getCpfSubstituido())
                .build();
        pessoaFisicaSubstituido.setNome(afastamentoData.getNomeSubstituto());

        final var substuido = Funcionario.builder()
                .matricula("ba"+afastamentoData.getCpfSubstituido())
                .pessoaFisica(pessoaFisicaSubstituido)
                .build();

        return ContratoTerceirizado.builder()
                .funcionario(substuido)
                .contrato(contrato)
                .build();
    }

    public ContratoTerceirizado buildSubstituto() {

        final var contrato = Contrato.builder()
                .numero(afastamentoData.getNumeroContrato())
                .build();

        final var pessoaFisicaSubstituto = PessoaFisica.builder()
                .cpf(afastamentoData.getCpfSubstituido())
                .build();
        pessoaFisicaSubstituto.setNome(afastamentoData.getNomeSubstituto());

        final var substituto = Funcionario.builder()
                .matricula("ba"+afastamentoData.getCpfSubstituido())
                .pessoaFisica(pessoaFisicaSubstituto)
                .build();

        return ContratoTerceirizado.builder()
                .funcionario(substituto)
                .contrato(contrato)
                .build();
    }
}
