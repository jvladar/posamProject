import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {NgForm} from "@angular/forms";
import {Visit} from "../visit";

@Component({
  selector: 'app-edit-visit',
  templateUrl: './edit-visit.component.html',
  styleUrls: ['./edit-visit.component.scss']
})
export class EditVisitComponent {
  visit : Visit | undefined;
  visite = {} as Visit;
  doctor_id : number | undefined;

  constructor(private route: ActivatedRoute, private router: Router, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    usersService.getVisit(Number(route.snapshot.paramMap.get("id_visit"))).subscribe(value => {
      this.visit = value;
    })
  }

  submit(form: NgForm){
    this.visite = form.value
    this.visite.id = <number>this.visit?.id
    if(form.invalid || !this.visite){
      return
    }
    console.log(this.visite)
    this.usersService.editVisit(this.visite)
      .subscribe(()=>{
        this.router.navigate(["/calendar", this.doctor_id])
      })
  }
}
