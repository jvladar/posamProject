import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarEntryComponent } from './navbar-entry.component';

describe('NavbarEntryComponent', () => {
  let component: NavbarEntryComponent;
  let fixture: ComponentFixture<NavbarEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NavbarEntryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
