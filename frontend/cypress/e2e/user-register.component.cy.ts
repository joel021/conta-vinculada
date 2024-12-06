import { login } from "./authentication"

describe('Counter', () => {

    beforeEach(() => {
        login()
    });

    it('Do not put name', () => {
        cy.visit('/user-register')
        cy.get("#emailId").clear()
        cy.get("#nameId").clear()
        cy.get("#emailId").type("testerAdmin@gmail.com")
        cy.get("#buttonRegisterId").click()
        cy.contains("Forneça seu nome.")
    });

    it('Do not put the e-email', () => {

        cy.visit('/user-register')
        cy.get("#nameId").clear()
        cy.get("#emailId").clear()
        cy.get("#nameId").type("Nome Qualquer")
        cy.get("#buttonRegisterId").click()
        cy.contains("Forneça seu email.")
    });

    it('Register and go to home page', () => {

        cy.visit('/user-register')
        cy.get("#nameId").clear()
        cy.get("#emailId").clear()
        cy.get("#nameId").type("Tester Admin")
        cy.get("#emailId").type("testerAdmin@gmail.com")
        cy.get("#buttonRegisterId").click()
        cy.url().should('not.include', 'user-register')
    
    });

});