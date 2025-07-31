import { login } from "./authentication"

describe('Liberacao tests', () => {

    beforeEach(() => {
        login()
    });

    it('Cadastrar Liberacao ', () => {
        cy.visit('liberacao/:idContrato/:idContratoTercerizado') 
        cy.get("#inputtipo").type("Décimo 13°")  
        cy.get('select').select(1)         
        cy.get("#buttonLiberacaoId").click()
        cy.wait(2000)
        
        
    });

    it('Cadastrar Liberacao ', () => {
        cy.visit('liberacao/:idContrato/:idContratoTercerizado') 
        cy.get("#inputtipo").type("FGTS")  
        cy.get('select').select(2)         
        cy.get("#buttonLiberacaoId").click()
        cy.wait(2000)
        
        
    });

    it('Cadastrar Liberacao ', () => {
        cy.visit('liberacao/:idContrato/:idContratoTercerizado') 
        cy.get("#inputtipo").type("Férias")  
        cy.get('select').select(3)         
        cy.get("#buttonLiberacaoId").click()
        cy.wait(2000)
        
        
    });
   

});