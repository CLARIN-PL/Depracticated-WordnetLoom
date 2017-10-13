import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RightAreaComponent } from './right-area.component';

describe('RightAreaComponent', () => {
  let component: RightAreaComponent;
  let fixture: ComponentFixture<RightAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RightAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RightAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
