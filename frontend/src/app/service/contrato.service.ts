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

  getIncGrupA(idContrato: any): Observable<any> {
    return this.http.get<any>(
       `${environment.apiUrl}/inc_grupo_a_contrato/contrato/${idContrato}`
     )
   }

   adicionarIncGrupA(dadosIncGrupoA: any) {
    return this.http.post<any>(
      `${environment.apiUrl}/inc_grupo_a_contrato/`,
      dadosIncGrupoA
    );
  }
}
  

