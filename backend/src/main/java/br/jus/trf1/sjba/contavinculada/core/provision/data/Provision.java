package br.jus.trf1.sjba.contavinculada.core.provision.data;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Provision {

    private String cargo;
    private double remuneracao;
    private double decimo;
    private double ferias;
    private double abFerias;
    private double subTotal;
    private double incGrupoA;
    private double multaFGTS;
    private double totalProvisaoMensal;
    private LocalDate inicioVigencia;
    private LocalDate criadoEm;
    private LocalDate date;

    private final double RATE_DECIMO = 0.0909d;
    private final double RATE_FERIAS = 0.0909d;
    private final double RATE_AB_FERIAS = 0.0303d;
    private final double RATE_MULTA_FGTS = 0.0349d;
    private final double RATE_INC_GRUPO_A;

    public Provision(double rateIncGrupoA) {
        RATE_INC_GRUPO_A = rateIncGrupoA;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void remuneracaoChanged() {

        ferias = remuneracao * RATE_FERIAS;
        abFerias = remuneracao * RATE_AB_FERIAS;
        decimo = remuneracao * RATE_DECIMO;
        multaFGTS = remuneracao * RATE_MULTA_FGTS;
        subTotalChanged();
    }

    public void subTotalChanged() {

        subTotal = decimo + ferias + abFerias;
        incGrupoA = RATE_INC_GRUPO_A * subTotal;
        totalProvisaoMensalChanged();
    }

    public void totalProvisaoMensalChanged() {
        totalProvisaoMensal = subTotal + incGrupoA + multaFGTS;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setRemuneracao(double remuneracao) {
        this.remuneracao = remuneracao;
        this.remuneracaoChanged();
    }

    public void setDecimo(double decimo) {
        this.decimo = decimo;
        subTotalChanged();
    }

    public void setFerias(double ferias) {
        this.ferias = ferias;
        subTotalChanged();
    }

    public void setAbFerias(double abFerias) {
        this.abFerias = abFerias;
        subTotalChanged();
    }

    public void setIncGrupoA(double incGrupoA) {
        this.incGrupoA = incGrupoA;
        totalProvisaoMensalChanged();
    }

    public void setMultaFGTS(double multaFGTS) {
        this.multaFGTS = multaFGTS;
        totalProvisaoMensalChanged();
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String toString() {
        return String.format("Provision(cargo='%s', remuneracao=%f, decimo=%f, ferias=%f, abFerias=%f, subTotal=%f, incGrupoA=%f, multaFGTS=%f, totalProvisaoMensal=%f, inicioVigencia=%s, criadoEm=%s, date=%s)",
                cargo, remuneracao, decimo, ferias, abFerias, subTotal, incGrupoA, multaFGTS, totalProvisaoMensal, inicioVigencia, criadoEm, date);
    }
}
