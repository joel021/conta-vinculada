import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Contrato } from '../model/contrato.model';

@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  constructor(private http: HttpClient) { }

getContrato(empresa: string): Observable<any> {
   return this.http.get<Contrato[]>(
      `${environment.apiUrl}/contrato/?nomePessoaJuridica=${empresa}`
    )
  }
}
  

