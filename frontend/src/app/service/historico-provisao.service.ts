import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HistoricoProvisaoService {

  constructor(private http: HttpClient) { }
 
  getHistProvisao(idContrato: any, periodo: any): Observable<any> {
    return this.http.get<any>(
       `${environment.apiUrl}/provisoes/${idContrato}?date=${periodo}`
     )
   }

   getProfessionalListByPeriod(
    period: any,
    page: number,
    quantity: number
  ): Observable<any> {
    return this.http.get(`${environment.apiUrl}/professionals/list/${period}?page=${page}&quantity=${quantity}`)
  }

}
