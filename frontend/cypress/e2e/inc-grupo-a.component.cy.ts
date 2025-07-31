import { login } from "./authentication"

describe('Counter', () => {

    beforeEach(() => {
        login()
    });
    
    it('Campos incidência e data vazio ', () => {
        cy.visit('/inc-grupo-a/1200')
        cy.get("#btnAdicionarIncGrupAId").should('be.visible')
        cy.wait(2000);
    });

    it('Campo data vazio ', () => {
        cy.visit('/inc-grupo-a/1200')
        cy.get("#incGrupoA").type("40")
        cy.get("#btnAdicionarIncGrupAId").should('be.visible')
        cy.wait(2000);
    });

    it('Campo incidência vazio ', () => {
        cy.visit('/inc-grupo-a/1200')
        const novoMesAno = '2024-02-15'; 
        cy.get("#data").type(novoMesAno)
        cy.get("#btnAdicionarIncGrupAId").should('be.visible')
        cy.wait(2000);
    });

    it('Alterar incidência grupo A', () => {
        cy.visit('/inc-grupo-a/1200')
        cy.get("#incGrupoA").type("40")
        const novoMesAno = '2024-02-15'; 
        cy.get("#data").type(novoMesAno)
        cy.get("#btnAdicionarIncGrupAId").click()
        cy.wait(2000);
    });

    it('Voltar para a tela lista de funcionários ', () => {
        cy.visit('/inc-grupo-a/1200?empresa=BRASILMED%20AUDITORIA%20MÉDICA%20E%20SERVIÇOS%20LTDA&contrato=13039365&idContrato=1200'
        );
        cy.get('#btnVoltarListFunciId').click();
        cy.wait(1000);
      });



});