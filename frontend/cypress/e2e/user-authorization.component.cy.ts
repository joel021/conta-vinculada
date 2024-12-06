import { login } from "./authentication"

describe('Test as admin', () => {

    beforeEach(() => {
        login()
    });

    it('Change and not confirm', () => {
        cy.visit('/user-authorization')

        //change to admin and not activated
        cy.get('#papeis_0').select(1)
        cy.get('#enabled_0').select(1)
        cy.contains("Confirmar alterações")
        
        //test case no confirmed
        cy.get('#modal_button_id_0').click()
        cy.get('#modal_no_button_id_0').click()
    });

    it('Change and confirm', () => {
        cy.visit('/user-authorization')

        //change to admin and not activated
        cy.get('#papeis_0').select(1)
        cy.get('#enabled_0').select(1)
        cy.contains("Confirmar alterações")
        
        //test case confirmed
        cy.get('#modal_button_id_0').click()
        cy.get('#modal_yes_button_id_0').click()

        //change to back
        cy.get('#enabled_0').select(0)
        cy.get('#modal_button_id_0').click()
        cy.get('#modal_yes_button_id_0').click()
    });

})