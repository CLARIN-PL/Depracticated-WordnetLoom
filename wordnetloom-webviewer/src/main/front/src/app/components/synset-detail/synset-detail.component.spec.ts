import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SynsetDetailComponent } from './synset-detail.component';

describe('SynsetDetailComponent', () => {
  let component: SynsetDetailComponent;
  let fixture: ComponentFixture<SynsetDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SynsetDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SynsetDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
