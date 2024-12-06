import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoProvisaoComponent } from './historico-provisao.component';

describe('HistoricoProvisaoComponent', () => {
  let component: HistoricoProvisaoComponent;
  let fixture: ComponentFixture<HistoricoProvisaoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoricoProvisaoComponent]
    });
    fixture = TestBed.createComponent(HistoricoProvisaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
