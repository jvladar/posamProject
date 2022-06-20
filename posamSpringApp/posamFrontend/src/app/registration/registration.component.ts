import {Component} from '@angular/core';
import {UsersService} from "../users.service";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Department, User} from "../user";
import {MessageDetail} from "../message-detail";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent{

  user = {} as User;
  messageDetail : MessageDetail | undefined;

  constructor(private router: Router, private usersService: UsersService) {
    this.user.department = Department.OCNE
  }

  submit(form: NgForm){
    if(form.invalid){
      return
    }
    this.usersService.registerUser(this.user)
      .subscribe((value)=>{
        this.messageDetail = value;
      })
  }

}
