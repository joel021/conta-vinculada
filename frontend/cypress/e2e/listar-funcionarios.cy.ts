import { login } from "./authentication"

describe('Counter', () => {

    beforeEach(() => {
        login()
    });

    it('Vizualizar histórico de provisão ', () => {
        cy.visit('/listar-funcionarios/767')
        cy.get("#btnHistProvisaoId").click()
        cy.wait(1000)
        
    });

    it('Realizar liberão ', () => {
        cy.visit('/listar-funcionarios/1019')
        cy.get("#btnLiberacaoId").first().click()
        cy.wait(1000)
        
    });

    it('Vizualizar histórico de liberão ', () => {
        cy.visit('/listar-funcionarios/1019')
        cy.get("#btnHistLibercaoId").first().click()
        cy.wait(1000)
        
    });

    it('Voltar para a tela de pesquisa ', () => {
        cy.visit('/listar-funcionarios/767?empresa=ELETROCONTROLE%20E%20ENGENHARIA%20COMERCIO%20LTDA&contrato=5886626&id=767')
        cy.get("#btnVoltarPesquContId").click()
        cy.wait(1000)
    });
      
});