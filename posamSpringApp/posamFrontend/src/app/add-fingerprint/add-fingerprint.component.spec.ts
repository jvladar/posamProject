import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFingerprintComponent } from './add-fingerprint.component';

describe('AddFingerprintComponent', () => {
  let component: AddFingerprintComponent;
  let fixture: ComponentFixture<AddFingerprintComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddFingerprintComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddFingerprintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
