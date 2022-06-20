import {Component, OnDestroy} from '@angular/core';
import {UsersService} from "./users.service";
import {Observable} from "rxjs";
import {MessageDetail} from "./message-detail";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnDestroy{
  title = 'posamFrontend';

  constructor(private userService: UsersService) {
  }

  ngOnDestroy() {
    // this.subscription.unsubscribe();
  }

  logout() {
    this.userService.logout()
  }
}
