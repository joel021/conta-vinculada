import { login } from './authentication';

describe('Counter', () => {
  beforeEach(() => {
    login();
  });

  it('Alterar mês da provisão', () => {
    cy.visit('/historicoprovisao/1017');
    const novoMesAno = '2025-07-15';
    cy.get('#monthYearInput').type(novoMesAno);
    cy.get('#monthYearInput').trigger('change');
    cy.wait(1000);
  });

  it('Voltar para a tela lista de funcionários ', () => {
    cy.visit('/historicoprovisao/1017?empresa=AVI%20SERVIÇOS%20DE%20SEGURANÇA%20LTDA&contrato=8390073&idContrato=1017'
    );
    cy.get('#btnVoltarListFunciId').click();
    cy.wait(1000);
  });
});
