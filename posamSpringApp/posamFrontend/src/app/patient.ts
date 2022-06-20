import {Visit} from "./visit";
import {User} from "./user";

export interface Patient {
  id:number;
  name:string;
  id_number:number;
  visits : Visit[];
  users : User[];
}
