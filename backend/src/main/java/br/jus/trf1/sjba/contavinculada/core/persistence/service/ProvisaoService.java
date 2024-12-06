package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.persistence.model.ContratoTerceirizado;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.IncGrupoAContrato;
import br.jus.trf1.sjba.contavinculada.core.provision.data.ContratoProvisions;
import br.jus.trf1.sjba.contavinculada.core.provision.data.FuncionarioProvision;
import br.jus.trf1.sjba.contavinculada.core.provision.FuncionarioProvisionMapper;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProvisaoService {

    @Autowired
    private IncGrupoAContratoService incGrupoAContratoService;

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    public ContratoProvisions getAllProvisoes(int idContrato, LocalDate date) throws NotFoundException, NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizadoList = contratoTerceirizadoService.findActiveByIdContrato(idContrato);

        if (!contratoTerceirizadoList.isEmpty()) {
            List<IncGrupoAContrato> incGrupoAContratoInc = incGrupoAContratoService.findByIdContrato(idContrato);

            if (incGrupoAContratoInc.isEmpty()) {
                throw new NotFoundException("Não foi encontrado um valor de Inc. grupo A para o contrato. Por favor, " +
                        "cadastre um valor.");
            }
            List<FuncionarioProvision> funcionarioProvisionList = new FuncionarioProvisionMapper(
                    contratoTerceirizadoList,
                    incGrupoAContratoInc
            ).allFuncionarioToSpecificPeriod(date);

            return ContratoProvisions.builder()
                    .contrato(contratoTerceirizadoList.get(0).getContrato())
                    .funcionarioProvisionList(funcionarioProvisionList)
                    .build();
        }
        return ContratoProvisions.builder().build();
    }
}
