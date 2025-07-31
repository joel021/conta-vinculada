import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ListarFuncionariosService {

  constructor(private http: HttpClient) { }

  getFuncionarios(idContrato: any ): Observable<any> {
    return this.http.get<any>(
       `${environment.apiUrl}/contrato_terceirizado/contrato/${idContrato}`
     )
   }

   getFuncionario(cpf: any): Observable<any>{
    return this.http.get<any>(
      `${environment.apiUrl}/funcionario/?cpf=${cpf}`
    )
   }

   patchFuncionario(idFuncionario: any , pessoaFisica : any , matricula: any , nivel: any, racaCor: any): Observable<any>{
    return this.http.patch<any>(
      `${environment.apiUrl}/funcionario/${idFuncionario}`,
      {pessoaFisica,
        matricula,
        nivel,
        racaCor
      } 
    );
   }
   postContratoTerceirizado(dadosContratoTerceirizado: any): Observable<any>{
    return this.http.post<any>(
      `${environment.apiUrl}/contrato_terceirizado/`,
      dadosContratoTerceirizado
    );
   }   

   deleteContratoTerceirizado(idContratoTercerizado: any): Observable<any>{
    return this.http.delete<any>(
      `${environment.apiUrl}/contrato_terceirizado/${idContratoTercerizado}`
    )
   }
   
   patchDesligarFuncionario(funcionarioId: any , contratoId: any , dataDesligamento: any): Observable<any> {
    const url = `${environment.apiUrl}/contrato_terceirizado/${funcionarioId}/${contratoId}?dataDesligamento=${dataDesligamento}`;
    return this.http.patch<any>(url, {});
  }

   }



 

