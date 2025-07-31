import { login } from "./authentication"

describe('Counter', () => {

    beforeEach(() => {
        login()
    });

    it('Vizualizar histórico de provisão ', () => {
        cy.visit('/listar-funcionarios/1017?empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA&contrato=8390073&idContrato=1017')
        cy.get("#btnHistProvisaoId").click()
        cy.wait(2000)
        
    });

    it('Vizualizar histórico de provisão sem inc. grupoA', () => {
        cy.visit('/listar-funcionarios/1200?empresa=BRASILMED%20AUDITORIA%20MÉDICA%20E%20SERVIÇOS%20LTDA&contrato=13039365&idContrato=1200')
        cy.get("#btnHistProvisaoId").click()
        cy.wait(2000)
        
    });

    it('Adicionar inc. grupoA', () => {
        cy.visit('/listar-funcionarios/1200?empresa=BRASILMED%20AUDITORIA%20MÉDICA%20E%20SERVIÇOS%20LTDA&contrato=13039365&idContrato=1200')
        cy.get("#btnIncGrupAId").click()
        cy.wait(2000)
        
    });

    it('Realizar liberação ', () => {
        cy.visit('/listar-funcionarios/1017?empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA&contrato=8390073&idContrato=1017')
        cy.get("#btnLiberacaoId").first().click()
        cy.wait(2000)       
    });

    it('Vizualizar histórico de liberação ', () => {
        cy.visit('/listar-funcionarios/1017?empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA&contrato=8390073&idContrato=1017')
        cy.get("#btnHistLibercaoId").first().click()
        cy.wait(2000)
        
    });

    it('Mudar quantidade por página', () => {
        cy.visit('/listar-funcionarios/1017?empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA&contrato=8390073&idContrato=1017');  
        cy.get("#quantityPerPageSelectId").select('20');
        cy.wait(2000)
    });

    it('Voltar para a tela de pesquisa ', () => {
        cy.visit('/listar-funcionarios/1017?idContrato=1017&contrato=8390073&empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA')
        cy.get("#btnVoltarPesquContId").click()
        cy.wait(2000)
    });
      
});
