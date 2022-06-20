import {Component, OnDestroy} from '@angular/core';
import {Patient} from "../patient";
import {ActivatedRoute, Router} from "@angular/router";
import {UsersService} from "../users.service";
import {Visit} from "../visit";
import {NgForm} from "@angular/forms";
import {Subject} from 'rxjs';


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnDestroy {

  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();

  patients : Patient[] | undefined;
  visite = {} as Visit;
  doctor_id: number | undefined;
  show_id : number = 0;
  visible:boolean = true;
  datum : Date | undefined;

  constructor(private route: ActivatedRoute,private router: Router, private usersService: UsersService) {
    this.doctor_id = Number(route.snapshot.paramMap.get("id"));
    usersService.getAllVisits(this.doctor_id).subscribe((value : any) => {
      this.patients = value;
      console.log(this.patients)
      this.dtTrigger.next(value);
    })
  }
  ngOnDestroy(): void {
    this.dtTrigger.unsubscribe();
  }

  showhideUtility(id : number){
    this.visible = this.visible?false:true;
    this.show_id = this.show_id?0:id;
  }

  submit(form: NgForm){
    this.visite = form.value
    if(form.invalid || !this.visite){
      return
    }
    console.log(this.visite)
    this.usersService.editVisit(this.visite)
      .subscribe(()=>{
        window.location.reload();
      })

    this.showhideUtility(0);
  }

  delete(item : Visit){
    if(confirm("Naozaj chcete odstrániť túto návštevu ?")) {
      this.usersService.deleteVisit(item.id)
        .subscribe(() => {
          window.location.reload();
        })
    }
  }

}
