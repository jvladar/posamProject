import {Fingerprint} from "./fingerprint";

export interface User {
  id : number;
  name : string;
  username : string;
  password : string;
  department : Department;
  fingerprints : Fingerprint[];
}

export enum Department {
  OCNE = "OČNE",
  CHIRURGIA = "Chirurgia",
  KOZNE = "Kozne",
  PSYCHIATRIA = "Psychiatria",
  KARDIOLOGIA = "Kardiologia",
  ORTOPEDIA = "Ortopedia",
}

export namespace Department {
  export function keys(): Array<string>{
    var keys = Object.keys(Department);
    return keys.slice(keys.length / 2, keys.length-1);
  }
}
