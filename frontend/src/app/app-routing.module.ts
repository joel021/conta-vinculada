import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContratosComponent } from './contas-vinculadas/contratos/contratos.component';
import { LiberacaoComponent } from './contas-vinculadas/liberacao/liberacao.component';
import { ListarFuncionariosComponent } from './contas-vinculadas/listar-funcionarios/listar-funcionarios.component';
import { LoginComponent } from './contas-vinculadas/login/login.component';
import { UserRegisterComponent } from './contas-vinculadas/user-register/user-register.component';
import { UserAuthorizationComponent } from './contas-vinculadas/user-authorization/user-authorization.component';
import { HistoricoLiberacaoComponent } from './contas-vinculadas/historico-liberacao/historico-liberacao.component';
import { HistoricoProvisaoComponent } from './contas-vinculadas/historico-provisao/historico-provisao.component';

const routes: Routes = [
  { path: '', component: LoginComponent},
  { path: 'login', component: LoginComponent},
  { path: 'user-register', component: UserRegisterComponent},
  { path: 'listar-funcionarios/:idContrato', component: ListarFuncionariosComponent },
  { path: 'contratos', component: ContratosComponent },
  { path: 'liberacao/:idContrato', component: LiberacaoComponent },
  { path: 'user-authorization', component: UserAuthorizationComponent},
  { path: 'historicoLiberacao/:idContrato', component: HistoricoLiberacaoComponent },
  { path: 'historicoprovisao/:idContrato', component: HistoricoProvisaoComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
