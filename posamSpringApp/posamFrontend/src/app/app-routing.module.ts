import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PatientsComponent} from "./patients/patients.component";
import {UsersComponent} from "./users/users.component";
import {EditUserComponent} from "./edit-user/edit-user.component";
import {AddVisitComponent} from "./add-visit/add-visit.component";
import {EditVisitComponent} from "./edit-visit/edit-visit.component";
import {EditPatientComponent} from "./edit-patient/edit-patient.component";
import {AddPatientComponent} from "./add-patient/add-patient.component";
import {CalendarComponent} from "./calendar/calendar.component";
import {HomeComponent} from "./home/home.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {AddFingerprintComponent} from "./add-fingerprint/add-fingerprint.component";
import {RegistrationComponent} from "./registration/registration.component";
import {AuthGuardService} from "./service/auth-guard.service";


const routes: Routes = [
  {path:"home",component:HomeComponent},
  {path:"registration",component:RegistrationComponent},
  {path:"addfingerprint",component:AddFingerprintComponent},
  {path:"doctors/:id",component:UsersComponent,canActivate:[AuthGuardService]},
  {path:"doctors/:id/edit",component:EditUserComponent,canActivate:[AuthGuardService]},
  {path:"patients/:id",component:PatientsComponent,canActivate:[AuthGuardService]},
  {path:"patients/:id/add",component:AddPatientComponent,canActivate:[AuthGuardService]},
  {path:"patients/:id/edit",component:EditPatientComponent,canActivate:[AuthGuardService]},
  {path:"patients/:id/:id_patient/edit",component:EditPatientComponent,canActivate:[AuthGuardService]},
  {path:"calendar/:id",component:CalendarComponent,canActivate:[AuthGuardService]},
  {path:"calendar/:id/add",component:AddVisitComponent,canActivate:[AuthGuardService]},
  {path:"calendar/:id/:id_visit/edit",component:EditVisitComponent,canActivate:[AuthGuardService]},
  {path:'',redirectTo: 'home', pathMatch: 'full'},
  {path:'**', component: NotFoundComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
