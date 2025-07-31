import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Contrato } from 'src/app/model/contrato.model';
import { ContratoService } from 'src/app/service/contrato.service';

@Component({
  selector: 'app-contratos',
  templateUrl: './contratos.component.html',
  styleUrls: ['./contratos.component.css'],
})
export class ContratosComponent implements OnInit {
  loading = false;
  errorMessage: string = '';
  message: string = '';
  loaded = false;
  nomeEmpresaDigitada: string = '';
  empresasEncontradas: Contrato[] = [];
  isDisabled: boolean = true;
  _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];
  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }
  constructor(
    private router: Router,
    private contratoService: ContratoService,
    private titleService:Title,
    private activatedRoute: ActivatedRoute
  ) {
    this.titleService.setTitle("Contratos") 
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['message'] != null) {
        this.message = params["message"]
      }
  
      if (params['empresa'] != null)  {
        
        this.pesquisarContrato(params['empresa']);
      }
    });
  }

  pesquisarContrato(empresa: string) {
    this.loading = true;
    this.errorMessage = '';
    this.contratoService.getContrato(empresa).subscribe({
      next: (resp) => {        
        this.loading = false;
        this.loaded = true;
        this.empresasEncontradas = resp;
        this.verificarStatus();
        if(resp.length === 0) {
          this.message =
            'A empresa:' +
            ' ' +
            empresa +
            ' ' +
            'não foi encontrada. Por favor, pesquise uma nova empresa.';
        }
      },
      error: (resp) => {
        this.errorMessage = 'Não foi possível obter os dados da empresa.';
        this.loading = false;
      },
    });
  }

  adicionarFuncionario(idContrato: any) {
    for ( let i = 0; i < this.empresasEncontradas.length; i++) { 
    if(idContrato == this.empresasEncontradas[i].idContrato ){
      const contrato = this.empresasEncontradas[i].numero
      const empresa = this.empresasEncontradas[i].pessoaJuridica?.nome
    this.router.navigate([`cadastro-funcionario/${idContrato}/`], { 
      queryParams: {
      empresa: empresa,
      contrato: contrato,            
      idContrato: idContrato
    },
   })
    }
    }
  }

  extrairData(dateTimeString?: string): string {
    if (dateTimeString) {
      const datePart = dateTimeString.split('T')[0];
      return datePart;
    }
    return '';
  }

  verificarStatus() {
    for (let i = 0; i < this.empresasEncontradas.length; i++) {
      const fimVigencia = this.extrairData(
        this.empresasEncontradas[i].fimVigencia
      );
      if (fimVigencia >= this.obterDataAtual()) {
        this.empresasEncontradas[i].status = 'Vigente';
      } else {
        this.empresasEncontradas[i].status = 'Não vigente';
      }
    }
  }
  obterDataAtual(): string {
    const now = new Date();
    const day = now.getDate();
    const month = now.getMonth() + 1;
    const year = now.getFullYear();
    return `${year}-${month.toString().padStart(2, '0')}-${day
      .toString()
      .padStart(2, '0')}`;
  }

  visualizarContrato(idContrato: any) {
    for (let i = 0; i < this.empresasEncontradas.length; i++) {
      if (idContrato === this.empresasEncontradas[i].idContrato) {
        const contrato = this.empresasEncontradas[i].numero;
        const empresa = this.empresasEncontradas[i].pessoaJuridica?.nome;
        this.router.navigate([`listar-funcionarios/${idContrato}`], {
          queryParams: {
            empresa: empresa,
            contrato: contrato,            
            idContrato: idContrato
          },
        });
      }
    }
  }
  verificarInput() {
    if (this.nomeEmpresaDigitada.length >= 3) {
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
  }

  clearMessage(){
    this.message = ''
  }
}
