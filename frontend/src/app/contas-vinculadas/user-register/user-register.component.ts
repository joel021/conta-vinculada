import { Component, OnInit } from '@angular/core'
import { Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { Title } from '@angular/platform-browser'
import { UserService } from 'src/app/service/user.service'
import { UsuarioUpdate } from 'src/app/model/user.update.model'
import { Usuario } from 'src/app/model/user.model'

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css'],
})
export class UserRegisterComponent implements OnInit {

  registerForm: FormGroup
  loading = false
  submitted = false
  returnUrl!: string
  errorMessage: any
  currentUser: Usuario

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: UserService,
    private title: Title
  ) {
    this.registerForm = this.formBuilder.group({
      nome: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(250)]],
      email: ['', [Validators.required, Validators.email]],
    })

    this.title.setTitle("Registro de informações")
    this.currentUser = this.authenticationService.currentUserValue
  }

  ngOnInit() {
    this.errorMessage = null
    this.checkIfAreLoggedIn()
  }

  get controls() {
    return this.registerForm.controls
  }


  onSubmit() {

    this.submitted = true
    if (this.registerForm.invalid) {
      return
    }
    this.loading = true

    if (this.currentUser.token != null && this.currentUser.token.length > 0) {
      this.updateAuthenticated()
    } else {
      this.updateNonAuthenticated()
    }
  }

  checkIfAreLoggedIn() {
    if (this.currentUser.email != '') {
      this.registerForm = this.formBuilder.group({
        nome: this.currentUser.nome,
        email: this.currentUser.email
      })
    }
  }

  updateAuthenticated() {

    this.authenticationService
      .updateAuthenticated(this.controls['nome'].value, this.controls['email'].value)
      .subscribe({
        next: (resp) => {
          this.authenticationService.saveUserInSession(resp.userDetails)
          this.loading = false
          this.router.navigate(['/contratos'], {
            queryParams: {
              message: 'Seus dados foram atualizados com sucesso. '
            },
          });
        },
        error: (error) => {
          this.errorHandler(error)
        },
      })
  }

  updateNonAuthenticated() {

    const userDetails = new UsuarioUpdate()

    userDetails.usuario = this.currentUser.usuario
    userDetails.nome = this.controls['nome'].value
    userDetails.email = this.controls['email'].value
    userDetails.senha = (<HTMLInputElement>document.getElementById("passwordId")).value
    userDetails.papeis = this.currentUser.papeis

    this.authenticationService
      .updateNonAuthenticated("username", userDetails)
      .subscribe({
        next: (resp) => {
          this.loading = false
          this.router.navigate(['/login'], {
            queryParams: {
              message: "O sistema guardou seu registro e os adminstradores foram notificados, "
              + "mas você ainda não tem acesso. Aguarde a liberação."
            }
          })
        },
        error: (error) => {
          this.errorHandler(error)
        },
      })
  }

  errorHandler(error: any) {

    this.loading = false

    if (error.errors != null && error.errors != undefined && error.errors.length > 0) {
      this.errorMessage = error.errors[0]
    } else {
      this.errorMessage = "Ocorreu algo de errado. Por favor, tente novamente."
    }
  }
}
