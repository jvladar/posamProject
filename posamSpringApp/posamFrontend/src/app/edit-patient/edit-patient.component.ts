import { Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {Patient} from "../patient";
import {NgForm} from "@angular/forms";
import {Subject} from "rxjs";

@Component({
  selector: 'app-edit-patient',
  templateUrl: './edit-patient.component.html',
  styleUrls: ['./edit-patient.component.scss']
})

export class EditPatientComponent {

  patient : Patient | undefined;
  doctor_id : number | undefined;
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(private route: ActivatedRoute, private router: Router, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    usersService.getPatient(Number(route.snapshot.paramMap.get("id_patient"))).subscribe(value => {
      this.patient = value;
      this.dtTrigger.next(value);
    })
  }

  submit(form: NgForm){
    if(form.invalid || !this.patient){
      return
    }
    this.usersService.editPatient(this.patient)
      .subscribe(()=>{
        this.router.navigate(["/patients", this.doctor_id])
      })
  }

}
