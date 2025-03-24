import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerStatusComponent } from './partner-status.component';

describe('PartnerStatusComponent', () => {
  let component: PartnerStatusComponent;
  let fixture: ComponentFixture<PartnerStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PartnerStatusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartnerStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
