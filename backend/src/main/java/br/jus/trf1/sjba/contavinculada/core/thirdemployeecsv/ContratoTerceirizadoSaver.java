package br.jus.trf1.sjba.contavinculada.core.thirdemployeecsv;

import br.jus.trf1.sjba.contavinculada.core.auditing.AuditCascadeSetter;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.AfastamentoService;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.ContratoService;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.ContratoTerceirizadoService;
import br.jus.trf1.sjba.contavinculada.core.persistence.service.FuncionarioService;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ContratoTerceirizadoSaver {

    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private AfastamentoService afastamentoService;

    private static final Logger LOGGER = Logger.getLogger(ContratoTerceirizadoSaver.class.getName());

    public boolean saveContratoTerceirizadoFromResources(Usuario criadoPor) {

        final ContratosTerceirizadosFromCsv contratosTerceirizadosFromCsv = new ContratosTerceirizadosFromCsv();
        ContratosTerceirizadosData contratosTerceirizadosData;
        try {
            contratosTerceirizadosData = contratosTerceirizadosFromCsv.getFromFileOnResources();
        } catch (NotAcceptableException | IOException e) {
            e.printStackTrace();
            return false;
        }
        final int contratoSuccesses = saveContratosTerceirizadosAndReturnSuccesses(
                criadoPor,
                contratosTerceirizadosData.getContratoTerceirizadoList()
        );
        final int afastamentoSuccesses = saveAfastamentosAndReturnSuccesses(
                criadoPor,
                contratosTerceirizadosData.getAfastamentoList()
        );

        return contratoSuccesses == contratosTerceirizadosData.getContratoTerceirizadoList().size()
                && afastamentoSuccesses == contratosTerceirizadosData.getAfastamentoList().size();
    }

    public int saveContratosTerceirizadosAndReturnSuccesses(Usuario criadoPor, List<ContratoTerceirizado> contratosTerceirizados) {

        int successes = 0;
        for (var contratoTerceirizado: contratosTerceirizados) {

            AuditCascadeSetter auditCascadeSetter = new AuditCascadeSetter(criadoPor);

            try {
                auditCascadeSetter.setCriadoPor(contratoTerceirizado);
                contratoTerceirizadoService.saveIfNotExists(contratoTerceirizado, criadoPor.siglaSecaoJudiciaria());
                successes += 1;
            } catch (NotFoundException | NotAcceptableException | IllegalAccessException e) {
                LOGGER.info("Save ContratoTerceirizado: "+e.getMessage());
            }
        }
        return successes;
    }

    public int saveAfastamentosAndReturnSuccesses(Usuario criadoPor, List<AfastamentoData> afastamentoDataList) {

        int successes = 0;
        for(AfastamentoData afastamentoData: afastamentoDataList) {

            final var afastamento = new AfastamentoBuilder(afastamentoData).buildAfastamento();
            AuditCascadeSetter auditCascadeSetter = new AuditCascadeSetter(criadoPor);
            try {
                auditCascadeSetter.setCriadoPor(afastamento);
                afastamentoService.saveIfNotExists(afastamento, criadoPor.siglaSecaoJudiciaria());
                successes += 1;
            } catch (NotFoundException | NotAcceptableException | IllegalAccessException e) {
                LOGGER.info("Save Afastamento"+e.getMessage());
            }
        }

        return successes;
    }

}
