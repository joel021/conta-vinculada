import { login } from "./authentication"

describe('Counter', () => {

    beforeEach(() => {
        login()
    });

    it('Pesquisar empresa que não existe ', () => {
        cy.visit('/contratos')
        cy.get("#pesquisarContratoId").clear()
        cy.get("#pesquisarContratoId").type("teste")
        cy.get("#btnpesquisarContratoId").click()
        
    });

    it('Campo empresa Vazio ', () => {
        cy.visit('/contratos')
        cy.get("#pesquisarContratoId").clear()
        cy.get("#btnpesquisarContratoId").should('be.visible')
    });

    it('Pesquisar empresa que existe ', () => {
        cy.visit('/contratos')
        cy.get("#pesquisarContratoId").clear()
        cy.get("#pesquisarContratoId").type("Limpeza")
        cy.get("#btnpesquisarContratoId").click()
        
    });

    it('Visualizar Contrato que não possui funcionário ', () => {
        cy.visit('/contratos')
        cy.get("#pesquisarContratoId").clear()
        cy.get("#pesquisarContratoId").type("Limpeza")
        cy.get("#btnpesquisarContratoId").click()
        cy.get("#btnVContratoId").first().click()
        
    });

    it('Visualizar Contrato possui funcionário ', () => {
        cy.visit('/contratos')
        cy.get("#pesquisarContratoId").clear()
        cy.get("#pesquisarContratoId").type("eletrocontrole")
        cy.get("#btnpesquisarContratoId").click()
        cy.get("#btnVContratoId").first().click()
        
    });
   
   

});