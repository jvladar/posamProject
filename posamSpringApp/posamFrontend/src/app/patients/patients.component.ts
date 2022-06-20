import {Component, OnDestroy} from '@angular/core';
import {User} from "../user";
import {ActivatedRoute} from "@angular/router";
import {UsersService} from "../users.service";
import {Patient} from "../patient";
import {Subject} from "rxjs";

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.scss']
})
export class PatientsComponent implements OnDestroy{

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  patients : Patient[] | undefined;
  doctor_id: number | undefined;

  constructor(private route: ActivatedRoute, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));

    usersService.getAllDoctorPatients(this.doctor_id).subscribe(value => {
      this.patients = value;
      this.dtTrigger.next(value);
    })
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  delete(item : Patient){
    if(confirm("Naozaj chcete odstrániť tohto pacienta ?")) {
      this.usersService.deletePatient(item.id)
        .subscribe(() => {
          window.location.reload();
        })
    }
  }

}
