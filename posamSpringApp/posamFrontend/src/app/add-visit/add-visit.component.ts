import { Component, OnInit } from '@angular/core';
import {Patient} from "../patient";
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {NgForm} from "@angular/forms";
import {Visit} from "../visit";

@Component({
  selector: 'app-add-visit',
  templateUrl: './add-visit.component.html',
  styleUrls: ['./add-visit.component.scss']
})
export class AddVisitComponent {

  patients : Patient[] | undefined;
  patient_id = {} as number;
  pat = {} as Patient;
  visit = {} as Visit;
  doctor_id : number;

  constructor(private router: Router, private route: ActivatedRoute, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    this.usersService.getAllDoctorPatients(this.doctor_id)
      .subscribe((value)=>{
        this.patients = value;
      })
    console.log(this.doctor_id)
  }

  submit(form: NgForm){
    if(form.invalid){
      return
    }
    this.patient_id = this.pat.id;
    this.usersService.addVisit(this.patient_id, this.visit)
      .subscribe((value)=>{
        this.router.navigate(["/calendar",this.doctor_id])
      })
  }

}
