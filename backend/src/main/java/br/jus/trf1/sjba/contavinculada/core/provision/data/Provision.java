package br.jus.trf1.sjba.contavinculada.core.provision.data;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
public class Provision {

    private final int SCALE = 10;
    private final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private String cargo;
    private BigDecimal remuneracao = BigDecimal.ZERO;
    private BigDecimal decimo = BigDecimal.ZERO;
    private BigDecimal ferias = BigDecimal.ZERO;
    private BigDecimal abFerias = BigDecimal.ZERO;
    private BigDecimal subTotal = BigDecimal.ZERO;
    private BigDecimal incGrupoA = BigDecimal.ZERO;
    private BigDecimal multaFGTS = BigDecimal.ZERO;
    private BigDecimal totalProvisaoMensal = BigDecimal.ZERO;
    private LocalDate inicioVigencia;
    private LocalDate criadoEm;
    private LocalDate date;

    private final BigDecimal RATE_DECIMO;
    private final BigDecimal RATE_FERIAS;
    private final BigDecimal RATE_AB_FERIAS;
    private final BigDecimal RATE_MULTA_FGTS;
    private final BigDecimal RATE_INC_GRUPO_A;

    public Provision(
            BigDecimal rateDecimo,
            BigDecimal rateFerias,
            BigDecimal rateAbFerias,
            BigDecimal rateMultaFgts,
            BigDecimal rateIncGrupoA
    ) {
        this.RATE_DECIMO = rateDecimo;
        this.RATE_FERIAS = rateFerias;
        this.RATE_AB_FERIAS = rateAbFerias;
        this.RATE_MULTA_FGTS = rateMultaFgts;
        this.RATE_INC_GRUPO_A = rateIncGrupoA;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void remuneracaoChanged() {
        ferias = remuneracao.multiply(RATE_FERIAS).setScale(SCALE, ROUNDING);
        abFerias = remuneracao.multiply(RATE_AB_FERIAS).setScale(SCALE, ROUNDING);
        decimo = remuneracao.multiply(RATE_DECIMO).setScale(SCALE, ROUNDING);
        multaFGTS = remuneracao.multiply(RATE_MULTA_FGTS).setScale(SCALE, ROUNDING);
        subTotalChanged();
    }

    public void subTotalChanged() {
        subTotal = decimo.add(ferias).add(abFerias).setScale(SCALE, ROUNDING);
        incGrupoA = RATE_INC_GRUPO_A.multiply(subTotal).setScale(SCALE, ROUNDING);
        totalProvisaoMensalChanged();
    }

    public void totalProvisaoMensalChanged() {
        totalProvisaoMensal = subTotal.add(incGrupoA).add(multaFGTS).setScale(SCALE, ROUNDING);
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setRemuneracao(BigDecimal remuneracao) {
        if (remuneracao == null) {
            this.remuneracao = BigDecimal.ZERO;
        } else {
            this.remuneracao = remuneracao.setScale(SCALE, ROUNDING);
        }

        this.remuneracaoChanged();
    }

    public void setDecimo(BigDecimal decimo) {
        this.decimo = decimo.setScale(SCALE, ROUNDING);
        subTotalChanged();
    }

    public void setFerias(BigDecimal ferias) {
        this.ferias = ferias.setScale(SCALE, ROUNDING);
        subTotalChanged();
    }

    public void setAbFerias(BigDecimal abFerias) {
        this.abFerias = abFerias.setScale(SCALE, ROUNDING);
        subTotalChanged();
    }

    public void setIncGrupoA(BigDecimal incGrupoA) {
        this.incGrupoA = incGrupoA.setScale(SCALE, ROUNDING);
        totalProvisaoMensalChanged();
    }

    public void setMultaFGTS(BigDecimal multaFGTS) {
        this.multaFGTS = multaFGTS.setScale(SCALE, ROUNDING);
        totalProvisaoMensalChanged();
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public void setCriadoEm(LocalDate criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return String.format(
                "Provision(cargo='%s', remuneracao=%s, decimo=%s, ferias=%s, abFerias=%s, subTotal=%s, incGrupoA=%s, multaFGTS=%s, totalProvisaoMensal=%s, inicioVigencia=%s, criadoEm=%s, date=%s)",
                cargo, remuneracao, decimo, ferias, abFerias, subTotal, incGrupoA, multaFGTS, totalProvisaoMensal, inicioVigencia, criadoEm, date
        );
    }
}
