import {Component, Injectable, Input, OnInit} from '@angular/core';
import {UsersService} from "../users.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar-doctor',
  templateUrl: './navbar-doctor.component.html',
  styleUrls: ['./navbar-doctor.component.scss']
})
@Injectable({
  providedIn: 'root'
})

export class NavbarDoctorComponent {

  @Input() doctor_id : number | undefined;

  constructor(private userService: UsersService, private router: Router) {
    this.doctor_id = 0;
  }

  logout(){
    this.userService.logout();
    this.router.navigate(["/"])
  }
}
