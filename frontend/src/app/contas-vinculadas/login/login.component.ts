import { Component, OnInit, Inject } from '@angular/core'
import { Router, ActivatedRoute } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { DOCUMENT } from '@angular/common'
import { UserService } from 'src/app/service/user.service'


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup
  loading = false
  submitted = false
  returnUrl!: string
  errorMessage: any
  message!: string

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authenticationService: UserService,
    private route: ActivatedRoute,
    @Inject(DOCUMENT) document: Document
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    })
  }

  ngOnInit() {

    this.authenticationService.logout()

    this.route.queryParams.subscribe(params => {
      this.message = params["message"]
    })
  }

  get controls() {
    return this.loginForm.controls
  }

  onSubmit() {

    this.errorMessage = ""
    this.submitted = true
    if (this.loginForm.invalid) {
      return
    }
    this.loading = true

    const section = (<HTMLInputElement>document.getElementById('sectionId')).value

    this.authenticationService.signin(section, this.controls['username'].value, this.controls['password'].value)
      .subscribe({
        next: (resp) => {
         // console.log(resp);
          this.loading = false
          this.authenticationService.saveUserInSession(resp.userDetails)
          if (resp.userDetails.email != null) {
            this.router.navigate(['/contratos'])
          } else {
            this.router.navigate(['/user-register'])
          }
        },
        error: (error) => {

          this.loading = false

          if (error.error != undefined) {
            
            if (error.error.errors != undefined) {
              this.errorMessage = error.error.errors[0]+""
            }else if(error.error.error != null){
              this.errorMessage = error.error.error+""
            }else{
              this.errorMessage = "Ocorreu um erro interno no sistema."
            }
          }
        },
      })
  }

}
