import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ListarFuncionariosService {

  constructor(private http: HttpClient) { }

  getFuncionarios(idContrato: any, page: number,quantity: number, ): Observable<any> {
    return this.http.get<any>(
       `${environment.apiUrl}/contrato_terceirizado/contrato/${idContrato}?page=${page}&quantity=${quantity}`
     )
   }
}
