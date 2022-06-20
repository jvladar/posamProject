import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {UsersService} from "../users.service";
import {NavbarDoctorComponent} from "../navbar-doctor/navbar-doctor.component";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private router: Router, private usersService: UsersService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      var x = this.usersService.isUserLoggedIn();
      var y: number = +x;
      if (y > 0)
          return true;
      this.router.navigate(["/home"]);
      return false;
  }


}
