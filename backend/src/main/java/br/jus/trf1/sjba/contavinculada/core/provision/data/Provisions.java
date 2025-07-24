package br.jus.trf1.sjba.contavinculada.core.provision.data;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Provisions {

    private final List<Provision> provisoes = new ArrayList<>();
    private BigDecimal totalProvision = BigDecimal.ZERO;

    public void addProvision(Provision provision) {
        provisoes.add(provision);
        totalProvision = totalProvision.add(provision.getTotalProvisaoMensal());
    }
}