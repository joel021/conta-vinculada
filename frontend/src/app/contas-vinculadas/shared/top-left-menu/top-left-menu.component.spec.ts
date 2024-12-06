import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopLeftMenuComponent } from './top-left-menu.component';

describe('TopLeftMenuComponent', () => {
  let component: TopLeftMenuComponent;
  let fixture: ComponentFixture<TopLeftMenuComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TopLeftMenuComponent]
    });
    fixture = TestBed.createComponent(TopLeftMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
