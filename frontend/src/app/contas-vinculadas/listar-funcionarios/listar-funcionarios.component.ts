import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ContratoTerceirizado } from 'src/app/model/contratoTerceirizado.model';
import { ListarFuncionariosService } from 'src/app/service/listar-funcionarios.service';

@Component({
  selector: 'app-visualizar-contrato',
  templateUrl: './listar-funcionarios.component.html',
  styleUrls: ['./listar-funcionarios.component.css'],
})
export class ListarFuncionariosComponent implements OnInit {
  form: FormGroup;
  funcionariosEncontradoContrato: ContratoTerceirizado[] = []
  loading = false;
  errorMessage: string = '';
  message: string = '';
  loaded = false;
  pagePosition: number = 0;
  totalPages: number = 1;
  pagesList: number[] = [0];
  previousSearchParams: any = {};
  _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];
  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private listarFuncService: ListarFuncionariosService,
    private router: Router
  ) {
    this.form = this.initiForm();
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params) => {
      this.listarFuncionarios(params['idContrato']);
    });
    
  }

  listarFuncionarios(idContrato: any) {
    console.log(idContrato);
    this.loading = true;
    this.errorMessage = '';
    const selectedQtPag = this.form.controls['selectedQtPag'].value;
    console.log('pagina:' + selectedQtPag);
    this.listarFuncService
      .getFuncionarios(idContrato, this.pagePosition, selectedQtPag)
      .subscribe({
        next: (resp) => {
          this.loading = false;
          this.loaded = true;
          this.totalPages = resp.totalPages;
          this.updateListPages();
          this.funcionariosEncontradoContrato = resp.content
          if (resp.content.length === 0) {
            this.activatedRoute.queryParams.subscribe((params) => {
              const id = params['id'];
              const contrato = params['contrato'];
              const empresa = params['empresa'];
              this.router.navigate([`contratos/`], {
                queryParams: {
                  message:
                    'Não existe funcionário para o contrato: ' + contrato,
                  id: id,
                  contrato: contrato,
                  empresa: empresa,
                },
              });
            });
          }     
          console.log(resp)    
         },
        error: (erro) => {
          this.errorMessage =
            'Não foi possível obter as informações desse contrato. Por favor, tente novamente recarregando a página.';
        },
      });
  }

  extrairData(dateTimeString?: string): string {
    if (dateTimeString) {
      const datePart = dateTimeString.split('T')[0];
      return datePart;
    }
    return '';
  }

  initiForm(): FormGroup {
    return this.formBuilder.group({
      selectedQtPag: 10,
    });
  }

  liberacao(idContrato: any) {
    this.router.navigate([`liberacao/${idContrato}`], {});
  }
 
  histLiberacao(idContrato: any) {
    this.router.navigate([`historicoLiberacao/${idContrato}`], {});
  }

  histProvisao(idContrato: any) {
    const contrato = this.funcionariosEncontradoContrato[0].contrato?.numero;
    const empresa = this.funcionariosEncontradoContrato[0].contrato?.pessoaJuridica?.nome;
    this.router.navigate([`historicoprovisao/${idContrato}`], {
      queryParams: {
        empresa: empresa,
        contrato: contrato,            
        id: idContrato
      },
    });
  }

  updateListPages() {
    if (this.totalPages >= 15) {
      const tile25 = Math.trunc(0.25 * this.totalPages);
      const tile75 = Math.trunc(0.75 * this.totalPages);
      this.pagesList = this.getListInterval(0, tile25);

      if (this.pagePosition == tile25) {
        this.pagesList = this.pagesList.concat(
          this.getListInterval(
            this.pagePosition,
            Math.min(this.pagePosition + 4, tile75)
          )
        );
      } else if (this.pagePosition > tile25 && this.pagePosition < tile75) {
        this.pagesList = this.pagesList.concat(
          this.getListInterval(
            Math.max(tile25, this.pagePosition - 3),
            Math.min(this.pagePosition + 4, tile75)
          )
        );
      }

      this.pagesList = this.pagesList.concat(
        this.getListInterval(tile75, this.totalPages)
      );
    } else {
      this.pagesList = this.getListInterval(0, this.totalPages);
    }
  }

  getListInterval(init: number, final: number): number[] {
    const intervalList = [];
    for (var i = init; i < final; i++) {
      intervalList.push(i);
    }
    return intervalList;
  }
  changePage(pagePosition: number) {
    this.pagePosition = pagePosition;
    this.ngOnInit();
  }

  nextPage() {
    this.pagePosition += 1;
    this.ngOnInit();
  }

  prevPage() {
    this.pagePosition -= 1;
  }

  is75Tile(pageNumber: number) {
    return pageNumber == Math.trunc(0.75 * this.totalPages);
  }

  toGoBack() {
    this.activatedRoute.queryParams.subscribe((params) => {
      const id = params['id'];
      const contrato = params['contrato'];
      const empresa = params['empresa'];
      this.router.navigate([`contratos/`], {
        queryParams: {
          id: id,
          contrato: contrato,
          empresa: empresa,
        },
      });
    });
  }
}
