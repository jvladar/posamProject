import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PatientsComponent } from './patients/patients.component';
import { UsersComponent } from './users/users.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { CalendarComponent } from './calendar/calendar.component';
import { EditVisitComponent } from './edit-visit/edit-visit.component';
import { EditPatientComponent } from './edit-patient/edit-patient.component';
import { AddPatientComponent } from './add-patient/add-patient.component';
import { HomeComponent } from './home/home.component';
import {LocalStorageModule} from "angular-2-local-storage";
import { NotFoundComponent } from './not-found/not-found.component';
import { RegistrationComponent } from './registration/registration.component';
import { AddFingerprintComponent } from './add-fingerprint/add-fingerprint.component';
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AddVisitComponent} from "./add-visit/add-visit.component";
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import {MatSortModule} from '@angular/material/sort';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSliderModule } from '@angular/material/slider';
import {DataTablesModule} from 'angular-datatables';
import {UserInterceptor} from "./user.interceptor";
import { NavbarEntryComponent } from './navbar-entry/navbar-entry.component';
import { NavbarDoctorComponent } from './navbar-doctor/navbar-doctor.component';
import {AuthGuardService} from "./service/auth-guard.service";

@NgModule({
  declarations: [
    AppComponent,
    PatientsComponent,
    UsersComponent,
    EditUserComponent,
    CalendarComponent,
    AddVisitComponent,
    EditVisitComponent,
    EditPatientComponent,
    AddPatientComponent,
    HomeComponent,
    NotFoundComponent,
    RegistrationComponent,
    AddFingerprintComponent,
    NavbarEntryComponent,
    NavbarDoctorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatSliderModule,
    MatSortModule,
    DataTablesModule,
    LocalStorageModule.forRoot({
      storageType: 'localStorage'
    }),
    HttpClientModule,
    BrowserAnimationsModule
  ],
  providers: [/*{provide: LocationStrategy, useClass: HashLocationStrategy},*/
    {provide: HTTP_INTERCEPTORS, useClass: UserInterceptor, multi: true}
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
