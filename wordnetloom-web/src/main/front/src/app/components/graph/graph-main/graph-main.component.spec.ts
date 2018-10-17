import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphMainComponent } from './graph-main.component';

describe('GraphMainComponent', () => {
  let component: GraphMainComponent;
  let fixture: ComponentFixture<GraphMainComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GraphMainComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
