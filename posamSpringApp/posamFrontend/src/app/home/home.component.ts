import { Component } from '@angular/core';
import {UsersService} from "../users.service";
import {Router} from "@angular/router";
import {MessageDetail} from "../message-detail";
import {NavbarDoctorComponent} from "../navbar-doctor/navbar-doctor.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  btnVal = "Skenovať";
  sv = {} as MessageDetail;

  constructor(private userService: UsersService, private router: Router) {
    this.sv.message = "";
    console.log(this.sv);
  }

  scan(){
    this.btnVal = 'Skenujem';
    this.userService.getDoctorId(this.sv)
      .subscribe( value => {
        if(value == 0){
          this.sv.message = "Používateľ sa nenašiel, skúste priložiť prst ešte raz"
          this.btnVal = "Skenovať"
          this.userService.logout();
        }
        else{
          this.router.navigate(["/doctors",value])
        }
      })

    /*this.userService.getDoctorTokenId(this.sv)
      .subscribe( value => {
        console.log(value);
        console.log("fafsf");
        this.router.navigate(["/doctors",23])
      })*/
  }


}
