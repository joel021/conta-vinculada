import {testUsers} from "./test_constants"

describe('login tests', () => {

    it('Auth with wrong credentials', () => {
        cy.visit('/login')
        cy.get("#usernameId").type("0000000000")
        cy.get("#passwordId").type("wrong_password")
        cy.get('select').select(1)
        cy.get("#buttonLoginId").click()
        cy.contains('Seu usuário ou senha da Justiça estão incorretos. O usuário deve ser a sua matrícula')
    });

    it('Auth without usernme', () => {
        cy.visit('/login')
        cy.get("#passwordId").type("wrong_password")
        cy.get('select').select(0)
        cy.get("#buttonLoginId").click()
        cy.contains('Forneça sua matrícula da Justiça.')
    });

    it('Auth without password', () => {
        cy.visit('/login')
        cy.get("#usernameId").type("0000000000")
        cy.get('select').select(0)
        cy.get("#buttonLoginId").click()
        cy.contains('Forneça sua senha.')
    });

    it('Auth with admin credentials', () => {
        cy.visit('/login')
        cy.get("#usernameId").type(testUsers.admin.username)
        cy.get("#passwordId").type(testUsers.admin.password)
        cy.get('select').select(1)
        cy.get("#buttonLoginId").click()
        
    });

    it('Auth with guest credentials', () => {
        cy.visit('/login')
        cy.get("#usernameId").type(testUsers.guest.username)
        cy.get("#passwordId").type(testUsers.guest.password)
        cy.get('select').select(1)
        cy.get("#buttonLoginId").click()
        cy.get('#liAdminUriId').should('not.exist')
    });

});