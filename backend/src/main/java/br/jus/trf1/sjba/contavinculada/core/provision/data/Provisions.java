package br.jus.trf1.sjba.contavinculada.core.provision.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Provisions {

    private final List<Provision> provisoes = new ArrayList<>();
    private double totalProvision;

    public void addProvision(Provision provision) {
        provisoes.add(provision);
        totalProvision += provision.getTotalProvisaoMensal();
    }
}
