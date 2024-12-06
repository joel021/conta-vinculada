import { TestBed } from '@angular/core/testing';

import { ListarFuncionariosService } from './listar-funcionarios.service';

describe('ListarFuncionariosService', () => {
  let service: ListarFuncionariosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListarFuncionariosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
