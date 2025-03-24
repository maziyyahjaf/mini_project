import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FloatingPromptComponent } from './floating-prompt.component';

describe('FloatingPromptComponent', () => {
  let component: FloatingPromptComponent;
  let fixture: ComponentFixture<FloatingPromptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FloatingPromptComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FloatingPromptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
