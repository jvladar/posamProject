import {Component, Input, OnInit} from '@angular/core';
import {User} from "../user";
import {ActivatedRoute} from "@angular/router";
import {UsersService} from "../users.service";
import {LocalStorageService} from "angular-2-local-storage";

@Component({
  selector: 'app-users',
  templateUrl: '/users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit{

  user : User | undefined;
  doctor_id: number | undefined;

  constructor(private route: ActivatedRoute, private localStorageService: LocalStorageService, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    usersService.getDoctorHomepage(this.doctor_id).subscribe(value => {
      this.user = value;
      console.log(this.user);
    })
  }
  ngOnInit(){
  }
}
