import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "./user";
import {Patient} from "./patient";
import {Visit} from "./visit";
import {MessageDetail} from "./message-detail";
import {BehaviorSubject, map, Observable, tap} from "rxjs";
import {NavbarDoctorComponent} from "./navbar-doctor/navbar-doctor.component";
import {AuthGuardService} from "./service/auth-guard.service";

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private userSubject = new BehaviorSubject<String | undefined>(undefined);
  private token: string | undefined;
  private id_session: Observable<number> | undefined;

  constructor(private http: HttpClient) { }

  /*getDoctorTokenId(message: MessageDetail): Observable<String> {
    console.log("mak")
    return this.http.post('/api/home',message,{responseType: 'text'})
      /!*.pipe(map(token => {
        this.token = token;
        console.log(this.token)
        if (!token) {
          return undefined
        }

        return JSON.parse(atob(token.split('.')[1]));
      }))
      .pipe(tap(user => {
        console.log(user);
        this.userSubject.next(user);
      }))*!/
  }*/

  getDoctorId(mes : MessageDetail){
    this.id_session = this.http.post<number>("/api/home",mes)
    sessionStorage.setItem("doctorid",String(this.id_session))
    return this.id_session;
  }

  isUserLoggedIn(){
    let idCheck = sessionStorage.getItem("doctorid");
    return !(idCheck === null);
  }

  logout() {
    sessionStorage.removeItem("doctorid")
  }

  getIdSession(){
    if(this.id_session)
      return this.id_session;
    return ;
  }


  addFingerprint(user : User){
    return this.http.post<MessageDetail>("/api/addfingerprint",user)
  }
  addPatient(id : number, patient : Patient){
    return this.http.post<Patient>("/api/patients/"+ id + "/add",patient)
  }
  addVisit(id : number, visit : Visit){
    return this.http.post<Patient>("/api/calendar/"+ id + "/add",visit)
  }
  registerUser(user : User){
    return this.http.post<MessageDetail>("/api/registration" ,user)
  }
  getDoctorHomepage(id : number){
    return this.http.get<User>("/api/doctors/" + id)
  }
  getAllDoctorPatients(id : number){
    return this.http.get<Patient[]>("/api/patients/" + id)
  }
  getAllVisits(id : number){
    return this.http.get<Patient[]>("/api/calendar/" + id)
  }
  getPatient(id : number){
    return this.http.get<Patient>("/api/patient/" + id)
  }
  getVisit(id : number){
    return this.http.get<Visit>("/api/calendar/" + id + "/edit")
  }
  editUser(user:User){
    return this.http.post<void>("/api/doctors/" + user.id + "/edit", user)
  }
  editPatient(patient : Patient){
    return this.http.post<void>("/api/patients/" + patient.id + "/edit", patient)
  }
  editVisit(visit : Visit){
    return this.http.post<void>("/api/calendar/" + visit.id + "/edit", visit)
  }
  deleteVisit(id:number){
    return this.http.delete<void>("/api/visit/" + id)
  }
  deletePatient(id:number){
    return this.http.delete<void>("/api/patient/" + id)
  }

}
