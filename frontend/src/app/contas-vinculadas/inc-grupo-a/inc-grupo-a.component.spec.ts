import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncGrupoAComponent } from './inc-grupo-a.component';

describe('IncGrupoAComponent', () => {
  let component: IncGrupoAComponent;
  let fixture: ComponentFixture<IncGrupoAComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncGrupoAComponent]
    });
    fixture = TestBed.createComponent(IncGrupoAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
