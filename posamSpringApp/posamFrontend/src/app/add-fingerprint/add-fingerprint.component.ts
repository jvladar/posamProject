import { Component, OnInit } from '@angular/core';
import {Department, User} from "../user";
import {MessageDetail} from "../message-detail";
import {Router} from "@angular/router";
import {UsersService} from "../users.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-add-fingerprint',
  templateUrl: './add-fingerprint.component.html',
  styleUrls: ['./add-fingerprint.component.scss']
})
export class AddFingerprintComponent{

  user = {} as User;
  messageDetail : MessageDetail | undefined;

  constructor(private router: Router, private usersService: UsersService) {
  }

  submit(form: NgForm){
    if(form.invalid){
      return
    }
    console.log(this.user)
    this.usersService.addFingerprint(this.user)
      .subscribe((value)=>{
        this.messageDetail = value
        console.log(value)
      })
  }

}
