import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ToastrModule } from 'ngx-toastr';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ContratosComponent } from './contas-vinculadas/contratos/contratos.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ListarFuncionariosComponent } from './contas-vinculadas/listar-funcionarios/listar-funcionarios.component';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './contas-vinculadas/home/layout/layout.component';
import { UserAuthorizationComponent } from './contas-vinculadas/user-authorization/user-authorization.component';
import { LoginComponent } from './contas-vinculadas/login/login.component';
import { UserRegisterComponent } from './contas-vinculadas/user-register/user-register.component';
import { TopLeftMenuComponent } from './contas-vinculadas/shared/top-left-menu/top-left-menu.component';
import { JwtInterceptor } from './security/jwt.interceptor';
import { AuthInterceptor } from './security/auth.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ModalComponent } from './contas-vinculadas/shared/modal/modal.component';
import { ToastComponent } from './contas-vinculadas/shared/toast/toast.component';
import { LiberacaoComponent } from './contas-vinculadas/liberacao/liberacao.component';
import { HistoricoLiberacaoComponent } from './contas-vinculadas/historico-liberacao/historico-liberacao.component';
import { HistoricoProvisaoComponent } from './contas-vinculadas/historico-provisao/historico-provisao.component';
import { IncGrupoAComponent } from './contas-vinculadas/inc-grupo-a/inc-grupo-a.component';
import { CadastroFuncionarioComponent } from './contas-vinculadas/cadastro-funcionario/cadastro-funcionario.component';



@NgModule({
  declarations: [
    AppComponent,
    TopLeftMenuComponent,
    ContratosComponent,
    ListarFuncionariosComponent, 
    LayoutComponent,
    UserAuthorizationComponent,
    UserRegisterComponent,
    LoginComponent,
    ModalComponent,
    ToastComponent,
    LiberacaoComponent,
    HistoricoLiberacaoComponent,
    HistoricoProvisaoComponent,
    IncGrupoAComponent,
    CadastroFuncionarioComponent    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    RouterModule,
    ModalModule.forRoot(),
    ToastrModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }


