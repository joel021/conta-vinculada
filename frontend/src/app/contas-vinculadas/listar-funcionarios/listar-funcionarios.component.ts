import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ContratoTerceirizado } from 'src/app/model/contratoTerceirizado.model';
import { ContratoService } from 'src/app/service/contrato.service';
import { ListarFuncionariosService } from 'src/app/service/listar-funcionarios.service';
import { ModalCallback, ModalResponse, ModalResultCallBack } from '../shared/modal/modalcallback.class';

@Component({
  selector: 'app-visualizar-contrato',
  templateUrl: './listar-funcionarios.component.html',
  styleUrls: ['./listar-funcionarios.component.css'],
})
export class ListarFuncionariosComponent implements OnInit, ModalCallback {
  
  funcionariosEncontradoContrato: any[] = []
  loading = false;
  errorMessage: string = '';
  message: string = '';
  loaded = false;    
  incGrupoA: any
  _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];

  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }

  constructor(
    private activatedRoute: ActivatedRoute,
    private listarFuncService: ListarFuncionariosService,
    private contratService: ContratoService,
    private titleService:Title,
    private router: Router
    
  ) {
    this.titleService.setTitle("Lista de funcionários")    
  }
 
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['message'] != null) {
        this.message = params["message"]         
      }
      if (params['idContrato'] != null)  {
      this.listarFuncionarios(params['idContrato']);
      }
     
     });
    
     }

  listarFuncionarios(idContrato: any) {
    this.loading = true;
    this.errorMessage = '';    
    this.listarFuncService
      .getFuncionarios(idContrato)
      .subscribe({
        next: (resp) => {          
          this.loading = false;
          this.loaded = true; 
          this.buscarIncGrupoA(idContrato)
          this.funcionariosEncontradoContrato = resp
          if (resp.length === 0) {
            this.activatedRoute.queryParams.subscribe((params) => {
              const idContrato = params['idContrato'];
              const contrato = params['contrato'];
              const empresa = params['empresa'];
              this.router.navigate([`contratos/`], {
                queryParams: {
                  message:
                    'Não existe funcionário para o contrato: ' + contrato,
                  idContrato: idContrato,
                  contrato: contrato,
                  empresa: empresa,
                },
              });
            });
          }              
         },
        error: (erro) => {
          this.errorMessage =
            'Não foi possível obter as informações desse contrato. Por favor, tente novamente recarregando a página.';
        },
      });
  }

  liberacao(idContrato: any , idContratoTercerizado: any) {
    this.router.navigate([`liberacao/${idContrato}/${idContratoTercerizado}`], {});
  }
 
  histLiberacao(idContrato: any) {
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome
    this.router.navigate([`historicoLiberacao/${idContrato}`], { queryParams: {
      empresa: empresa,
      contrato: contrato,            
      idContrato: idContrato
    },
  });
  }

  histProvisao(idContrato: any) {
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome
    this.router.navigate([`historicoprovisao/${idContrato}`], {
      queryParams: {
        empresa: empresa,
        contrato: contrato,            
        idContrato: idContrato
      },
    });
  }

  novoTermoAditivo(idContrato: any, cpf: any) {    
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero;
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome;
    this.router.navigate([`cadastro-funcionario/${idContrato}/`],{
    queryParams: {
      empresa: empresa,
      contrato: contrato,            
      idContrato: idContrato,
      cpf: cpf
    },
    });   
   
  }   
  adicionarFuncionario(idContrato: any) {    
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero;
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome;
    this.router.navigate([`cadastro-funcionario/${idContrato}/`], { 
      queryParams: {
      empresa: empresa,
      contrato: contrato,            
      idContrato: idContrato
    },
  })
  }
  editarFuncionario(cpf: any) {
    const idContrato = this.funcionariosEncontradoContrato[0].contrato?.idContrato;
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero;
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome;    
    for ( let i = 0; i < this.funcionariosEncontradoContrato.length; i++) {                
      if ( cpf === this.funcionariosEncontradoContrato[i].pessoaFisica?.cpf ) {
      const idFuncionario = this.funcionariosEncontradoContrato[i].idFuncionario        
    this.router.navigate([`cadastro-funcionario/${idContrato}/`], { 
      queryParams: {
      empresa: empresa,
      contrato: contrato,            
      idContrato: idContrato,
      idFuncionario : idFuncionario,
      cpf:cpf
    },
     })
    }
   }
  }

 buscarIncGrupoA(idContrato: any){
    this.contratService
      .getIncGrupA(idContrato)
      .subscribe({
        next: (resp) => {
          this.loading = false;
          this.loaded = true;
          if(resp == 0){
            this.incGrupoA = 0
          }else{
            this.incGrupoA = resp[0].incGrupoA
          }          
        }, 
        error: (erro) => {
          
        },
      });

  }

  adicionarIncGrupoA(idContrato: any){
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero;
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome;
    this.router.navigate([`inc-grupo-a/${idContrato}`], {
      queryParams: {
        empresa: empresa,
        contrato: contrato,            
        idContrato: idContrato
      },
    });
  }
  
  deletarContratoTercerizado(i : any){
    const idContratoTercerizado = this.funcionariosEncontradoContrato[i].contratoTerceirizadoList[0].id
    this.listarFuncService.deleteContratoTerceirizado(idContratoTercerizado)
    .subscribe({
      next: (resp) => {        
        this.loading = false;
        this.loaded = true;
        this.message = resp.message 
        this.activatedRoute.queryParams.subscribe(params => {
        if (params['idContrato'] != null)  {
          this.listarFuncionarios(params['idContrato']);
          } 
        });
              
      }, 
      error: (erro) => {
        },
    });
  }

  atualizarPagina(){
    window.location.reload()
  }

  desligarFuncionario(dataDesligamento: any , i: any){
    const funcionarioId = this.funcionariosEncontradoContrato[i].idFuncionario
    const contratoId = this.funcionariosEncontradoContrato[i].contrato.idContrato          
    this.listarFuncService.patchDesligarFuncionario(funcionarioId, contratoId, dataDesligamento )
    .subscribe({
      next: (resp) => {    
        this.loading = false;
        this.loaded = true;
        this.message = 'O funcionário ' + this.funcionariosEncontradoContrato[i].pessoaFisica.nome + ' foi desligado com sucesso!'
        this.atualizarPagina()              
      }, 
      error: (erro) => {
        },
    });
  }

  onResultUserSelect(response: ModalResponse): void {    
    if(response.dataDesligamento != '') {
      this.desligarFuncionario(response.dataDesligamento , response.position)
    }else if (response.selected == ModalResultCallBack.yes){
        this.deletarContratoTercerizado(response.position)      
    } else {
      this.message = ''
      this.errorMessage = ''
    }
    
  }


  clearMessage(){
    this.message = ''    
  } 
   toGoBack() {
    this.activatedRoute.queryParams.subscribe((params) => {
      const idContrato = params['idContrato'];
      const contrato = params['contrato'];
      const empresa = params['empresa'];
      this.router.navigate([`contratos/`], {
        queryParams: {
          idContrato: idContrato,
          contrato: contrato,
          empresa: empresa,
        },
      });
    });
  }

  formatarNumero(numero: number): string {
    const formato = { minimumFractionDigits: 2, maximumFractionDigits: 2 };
    return numero.toLocaleString('pt-BR', formato);
  }

  truncateText(text: string): string {
    const maxLength = 25; 
    if (text.length > maxLength) {
        const truncatedText = text.substr(0, maxLength); 
        return `${truncatedText}`; 
    }
    return text;
}
}
