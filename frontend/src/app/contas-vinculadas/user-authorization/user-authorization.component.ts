import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { HttpStatusCode } from '@angular/common/http';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { ModalCallback, ModalResponse, ModalResultCallBack } from '../shared/modal/modalcallback.class';
import { UserService } from 'src/app/service/user.service';
import { Usuario } from 'src/app/model/user.model';


@Component({
  selector: 'app-user-authorization',
  templateUrl: './user-authorization.component.html',
  styleUrls: ['./user-authorization.component.css']
})
export class UserAuthorizationComponent implements OnInit, ModalCallback {

  users!:any
  currentUser:any
  @ViewChild('userPage') inputFields!:ElementRef;
  loading:boolean = true
  message!: string
  errorMessage!: string
  isToast: boolean =false
  _toasts:Array<any> = [
    {title:'', message: '', isShow:true}
  ]
  
  get toasts(){
    return this._toasts.filter(f => f.isShow)
  }

  constructor(
    private userService:UserService,
    private router: Router,
    private title: Title
  ) {
    this.currentUser = userService.currentUserValue
    this.title.setTitle("Autorização de usuários")
  }

  onResultUserSelect(response: ModalResponse): void {

    if (response.selected == ModalResultCallBack.yes){
      this.updateUser(response.position)
    } else {
      this.users[response.position].loading = false
      this.message = ''
      this.errorMessage = ''
    }
    
  }

  updateUser(i:number) {

    const p = i;
    this.users[p].loading = true
    this.message = ''
    this.errorMessage = ''
    
    const userUpdated = new Usuario()
    userUpdated.usuario = this.users[p].usuario
    userUpdated.email = this.users[p].email
    userUpdated.nome = this.users[p].name
    userUpdated.enabled = JSON.parse((<HTMLInputElement>document.getElementById("enabled_"+p)).value)
    userUpdated.papeis = [(<HTMLInputElement>document.getElementById("papeis_"+p)).value]
    
    this.userService.updateAuthorization(userUpdated).subscribe({
      next: (resp) => {
        this.message = 'O usuário '+this.users[p].nome + ' matrícula '+this.users[p].usuario+" foi atualizado com sucesso."
        this.users[i] = userUpdated
      },
      error: (error) => {
        this.users[p].loading = false
        this.errorMessage = "Não foi possível atualizar o usuário " + this.users[p].nome + " matrícula "+this.users[p]
          + ". "+error.error.error
        this.handleUnauthorized(error)
      }
    })
  }

  ngOnInit(): void {
    this.retrieveUsers()
  }

  retrieveUsers() {

    this.loading = true
    this.message = ''
    this.errorMessage = ''
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.loading = false
        this.users = users
      },
      error: (error) => {
        this.loading = false
        this.errorMessage = "Não foi possível listar os usuários cadastrados no sistema."
        this.handleUnauthorized(error)
      }
    })
  }

  changed(i:number) {

    let papeisField = (<HTMLInputElement>document.getElementById("papeis_"+i)).value
    let enabledField = (<HTMLInputElement>document.getElementById("enabled_"+i)).value

    if (this.users[i].enabled+"" == enabledField && this.users[i].papeis == papeisField) {
      this.users[i].changed = false
    } else {
      this.users[i].changed = true
    }
    this.toasts.forEach(f => {
      f.isShow = true
    })
  }

  handleUnauthorized(error:any) {
    if (
      error.status == HttpStatusCode.Unauthorized ||
      error.status == HttpStatusCode.Forbidden
    ) {
      this.router.navigate(['/login'])
    }
  }

}
