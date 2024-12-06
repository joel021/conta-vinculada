package br.jus.trf1.sjba.contavinculada.core.persistence.service;

import br.jus.trf1.sjba.contavinculada.core.liberation.*;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.DecimoContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.FeriasContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.liberation.data.FGTSContratoLiberacao;
import br.jus.trf1.sjba.contavinculada.core.persistence.model.*;
import br.jus.trf1.sjba.contavinculada.core.persistence.repository.LiberacaoRepository;
import br.jus.trf1.sjba.contavinculada.exception.NotAcceptableException;
import br.jus.trf1.sjba.contavinculada.exception.NotFoundException;
import br.jus.trf1.sjba.contavinculada.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LiberacaoService {

    @Autowired
    private LiberacaoRepository liberacaoRepository;
    @Autowired
    private ContratoTerceirizadoService contratoTerceirizadoService;
    @Autowired
    private UserService userService;
    @Autowired
    private IncGrupoAContratoService incGrupoAContratoService;
    @Autowired
    private OficioMovimentacaoService oficioMovimentacaoService;

    public Liberacao registerLiberacao(Liberacao liberacao, LocalDate dataDesligamento) throws NotFoundException {

        if (liberacao.getDataLiberacao() == null) {
            liberacao.setDataLiberacao(LocalDate.now());
        }
        ContratoTerceirizado contratoTerceirizado = contratoTerceirizadoService.findById(liberacao.getContratoTerceirizado().getId());

        if (TipoLiberacao.FGTS.equals(liberacao.getTipo()) && dataDesligamento != null) {
            contratoTerceirizadoService.setDataDesligamento(
                    contratoTerceirizado.getFuncionario().getIdFuncionario(),
                    contratoTerceirizado.getContrato().getIdContrato(),
                    dataDesligamento
                    );
        }

        liberacao.setContratoTerceirizado(contratoTerceirizado);
        liberacao.setOficioMovimentacao(oficioMovimentacaoService.findOrRegister(liberacao.getOficioMovimentacao()));
        return liberacaoRepository.save(liberacao);
    }

    public List<Liberacao> findByContratoIdAndTipoLiberacao(int idContrato, TipoLiberacao tipoLiberacao) {

        Contrato contrato = new Contrato();
        contrato.setIdContrato(idContrato);

        return liberacaoRepository.findNonDeletedByContratoAndTipoLiberacao(contrato, tipoLiberacao);
    }

    public List<Liberacao> findbyContratoAndTipoLiberacao(int idContrato, TipoLiberacao tipoLiberacao) {

        Contrato contrato = new Contrato();
        contrato.setIdContrato(idContrato);

        return liberacaoRepository.findNonDeletedByContratoAndTipoLiberacao(contrato, tipoLiberacao);
    }

    public DecimoContratoLiberacao findDecimoTerceiro(int idContrato, LocalDate until) throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizados = contratoTerceirizadoService.findNonDeletedByIdContrato(idContrato);
        if (contratoTerceirizados.isEmpty()) {
            return new DecimoContratoLiberacao();
        }

        List<IncGrupoAContrato> incGrupoAContratoList = incGrupoAContratoService.findByIdContrato(idContrato);
        DecimoTerceiroLiberationHandler decimoTerceiroLiberationHandler = new DecimoTerceiroLiberationHandler(
                contratoTerceirizados,
                incGrupoAContratoList
        );
        final Contrato contrato = contratoTerceirizados.get(0).getContrato();

        List<Liberacao> liberacaoDecimoList = findbyContratoAndTipoLiberacao(
                idContrato,
                TipoLiberacao.DECIMO_TERCEIRO
        );
        return new DecimoContratoLiberacao()
                .funcionarioProvisions(decimoTerceiroLiberationHandler.calc(liberacaoDecimoList, until))
                .contrato(contrato);
    }

    public FeriasContratoLiberacao findFerias(int idContrato, LocalDate until) throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizados = contratoTerceirizadoService.findNonDeletedByIdContrato(idContrato);
        if (contratoTerceirizados.isEmpty()) {
            return new FeriasContratoLiberacao();
        }

        List<Liberacao> liberacaoFeriasList = findbyContratoAndTipoLiberacao(idContrato, TipoLiberacao.FERIAS);
        List<IncGrupoAContrato> incGrupoAContratoList = incGrupoAContratoService.findByIdContrato(idContrato);
        FeriasLiberationHandler feriasLiberationHandler = new FeriasLiberationHandler(
                contratoTerceirizados,
                incGrupoAContratoList
        );

        final Contrato contrato = contratoTerceirizados.get(0).getContrato();
        return new FeriasContratoLiberacao()
                .feriasPeriodProvisionList(feriasLiberationHandler.calc(liberacaoFeriasList, until))
                .contrato(contrato);
    }

    public FGTSContratoLiberacao findFGTS(int idContrato, LocalDate fechamento) throws NotAcceptableException {

        List<ContratoTerceirizado> contratoTerceirizados = contratoTerceirizadoService.findNonDeletedByIdContrato(idContrato);
        if (contratoTerceirizados.isEmpty()) {
            return new FGTSContratoLiberacao();
        }

        List<IncGrupoAContrato> incGrupoAContratoList = incGrupoAContratoService.findByIdContrato(idContrato);
        FGTSLiberationHandler fgtsLiberationHandler = new FGTSLiberationHandler(
                contratoTerceirizados,
                incGrupoAContratoList
        );
        final Contrato contrato = contratoTerceirizados.get(0).getContrato();
        List<Liberacao> liberacaoFGTSList = findbyContratoAndTipoLiberacao(idContrato, TipoLiberacao.FGTS);
        return new FGTSContratoLiberacao()
                .funcionarioProvisions(fgtsLiberationHandler.calc(liberacaoFGTSList, fechamento))
                .contrato(contrato);
    }
}
