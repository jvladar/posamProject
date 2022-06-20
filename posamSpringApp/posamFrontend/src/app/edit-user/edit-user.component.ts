import { Component, OnInit } from '@angular/core';
import {Department, User} from "../user";
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent {

  user: User | undefined;
  doctor_id: number;

  constructor(private route: ActivatedRoute, private userService: UsersService,
              private router: Router) {

    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    userService.getDoctorHomepage(this.doctor_id)
      .subscribe(value => {
        this.user = value;
      })
  }

  submit(form: NgForm){
    if(form.invalid || !this.user){
      return
    }
    this.userService.editUser(this.user)
      .subscribe(()=>{
        this.router.navigate(["/doctors", this.doctor_id])
      })
  }


}
