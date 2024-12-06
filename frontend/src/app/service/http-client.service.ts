import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from 'src/environments/environment.development';
import { IliberacaoModel } from '../contas-vinculadas/liberacao/iLiberacao.model';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  
  private readonly apiUrl: string;

  constructor(private http: HttpClient) {

    this.apiUrl = `${environment.apiUrl}`;
   }

  

   getDadosContrato(idContrato: any):Observable<any> {
    return this.http.get(`${this.apiUrl}/contrato/${idContrato}`);
  
  }

  

  getContrato(empresa: string): Observable<any> {
   return this.http.get<any>(
      `${environment.apiUrl}/contrato/?nomePessoaJuridica=${empresa}`
    )
  }

  /* 15122023 */ 
  save(novaLiberacao: IliberacaoModel) { 
    return this.http.post<IliberacaoModel>(`${this.apiUrl}/liberacao/teste`, novaLiberacao);
  }


  buscarHistoricoLiberacao(idContrato: any):Observable<any> {
    return this.http.get(`${this.apiUrl}/contrato/${idContrato}`);
  
  }

  

}
