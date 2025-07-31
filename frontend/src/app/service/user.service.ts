import { Injectable } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { BehaviorSubject, Observable } from 'rxjs'
import { environment } from 'src/environments/environment'

import { AuthInterceptor } from '../security/auth.interceptor'
import { Usuario } from '../model/user.model'
import Papel from '../model/role.authorities'
import { UsuarioUpdate } from '../model/user.update.model'

@Injectable({ providedIn: 'root' })
export class UserService {

  private currentUserSubject: BehaviorSubject<Usuario>
  public currentUser: Observable<Usuario>

  constructor(private http: HttpClient) {
    const storedUser = localStorage.getItem('currentUser')
    const initialUser = storedUser ? JSON.parse(storedUser) : null
    this.currentUserSubject = new BehaviorSubject<Usuario>(initialUser)
    this.currentUser = this.currentUserSubject.asObservable()
  }

  public get currentUserValue(): Usuario {
    return this.currentUserSubject.value
  }

  public get userId(): string {
    return this.currentUserSubject.value
      ? this.currentUserSubject.value.usuario
      : ''
  }

  getUsers(): Observable<Object>{
    return this.http.get(`${environment.apiUrl}/users/`)
  }

  signin(secao: string, username: string, password: string): Observable<any> {

    return this.http.post<any>(`${environment.apiUrl}/users/signin`, {
      dominio: secao,
      usuario: username,
      senha: password,
    })
  }

  updateAuthenticated(userUpdated: Usuario): Observable<any> {
    return this.http.patch<any>(`${environment.apiUrl}/users/`, userUpdated)
  }

  updateNonAuthenticated(username: string, userDetails:Usuario): Observable<any> {
    return this.http.patch<any>(`${environment.apiUrl}/users/update/${username}`, userDetails)
  }
  
  refreshToken(): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}/users/refresh_token`, {
      username: this.currentUserValue.usuario,
      password: this.currentUserValue.senha,
    })
  }

  saveUserInSession(user: Usuario) {
    localStorage.setItem('currentUser', JSON.stringify(user))
    AuthInterceptor.user = user
    this.currentUserSubject.next(user)
  }

  isAdmin() {
    return this.checkPermission(Papel.ADMIN)
  }

  checkPermission(permissionName: string) {
    if (this.currentUserValue && permissionName == Papel.ADMIN) {
      return true
    } else {
      return false
    }
  } 

  updateAuthorization(user: Usuario): Observable<any> {
    return this.http.patch<any>(`${environment.apiUrl}/users/authorization`, user)
  }

  readById(id: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/users/${id}`)
  }

  fetchAuthsUser(username: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/users/${username}`)
  }
 
  logout() {
    localStorage.removeItem('currentUser')
    this.currentUserSubject.next(new Usuario())
  }

 
}
