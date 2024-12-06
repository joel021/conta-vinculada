import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoLiberacaoComponent } from './historico-liberacao.component';

describe('HistoricoLiberacaoComponent', () => {
  let component: HistoricoLiberacaoComponent;
  let fixture: ComponentFixture<HistoricoLiberacaoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoricoLiberacaoComponent]
    });
    fixture = TestBed.createComponent(HistoricoLiberacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
