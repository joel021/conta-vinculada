import { TestBed } from '@angular/core/testing';

import { HistoricoProvisaoService } from './historico-provisao.service';

describe('HistoricoProvisaoService', () => {
  let service: HistoricoProvisaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HistoricoProvisaoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
