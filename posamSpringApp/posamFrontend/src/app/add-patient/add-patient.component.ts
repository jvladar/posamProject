import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {NgForm} from "@angular/forms";
import {Patient} from "../patient";

@Component({
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.scss']
})
export class AddPatientComponent {

  patient = {} as Patient;
  doctor_id : number;

  constructor(private router: Router, private route: ActivatedRoute, private usersService: UsersService) {
      this.doctor_id = Number(route.snapshot.paramMap.get("id"));
  }

  submit(form: NgForm){
    if(form.invalid){
      return
    }
    this.usersService.addPatient(this.doctor_id, this.patient)
      .subscribe((value)=>{
        console.log(value.id)
        this.router.navigate(["/patients",this.doctor_id,value.id,"edit"])
      })
  }

}
