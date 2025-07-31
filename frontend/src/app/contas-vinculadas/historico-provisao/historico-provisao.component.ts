import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { HistoricoProvisaoService } from 'src/app/service/historico-provisao.service';


@Component({
  selector: 'app-historico-provisao',
  templateUrl: './historico-provisao.component.html',
  styleUrls: ['./historico-provisao.component.css']
})
export class HistoricoProvisaoComponent {
  form: FormGroup;
  provisaoEncontradaContrato: any
  loading = false;
  errorMessage: string = '';
  message: string = '';
  loaded = false;
  pagePosition: number = 0;
  totalPages: number = 1;
  selectedPeriod: string = ''
  pagesList: number[] = [0];
  previousSearchParams: any = {};
  _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];
  provisao: any;
  provisaoSecundaria: any;
  somaTotalProvisaoMensal: any;
  periodo: any
  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private titleService:Title,
    private histprovisaoService: HistoricoProvisaoService
  ) {
    this.titleService.setTitle("Provisões") 
    this.form = this.initiForm();
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params) => {
      this.histoProvisao(params['idContrato']);
    });

    this.initiForm()
  }

  initiForm(): FormGroup {
    return this.formBuilder.group({
      selectedPeriod: [this.getCurrentPeriod()],      
    });
  }

  histoProvisao(idContrato: any) {
    this.loading = true;
    this.errorMessage = '';
    const selectedPeriod = this.form.controls['selectedPeriod'].value;
    this.getMonthName(selectedPeriod)
    this.histprovisaoService
      .getHistProvisao(idContrato, selectedPeriod)
      .subscribe({
        next: (resp) => {          
          this.loading = false;
          this.loaded = true;          
          this.provisaoEncontradaContrato = resp 
          this.somaTotalProvisaoMensal = this.provisaoEncontradaContrato.funcionarioProvisionList.reduce((total: any, provisao: { provisoes: { totalProvisaoMensal: any; }[]; }) => {
            if (provisao.provisoes.length > 0) {
                return total + provisao.provisoes[0].totalProvisaoMensal;
            } else {
                return total;
            }
        }, 0)            
         },
        error: (erro) => {
          this.activatedRoute.queryParams.subscribe(params => {
            const idContrato = params['idContrato'];
            const contrato = params['contrato'];
            const empresa = params['empresa'];
            this.router.navigate([`listar-funcionarios/${idContrato}`], {
              queryParams: {
                message: erro.error.error,
                contrato,
                empresa,
                idContrato
              },
            });
           
          });
        }
        },
      );
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
    this.ngOnInit();
  }

  is75Tile(pageNumber: number) {
    return pageNumber == Math.trunc(0.75 * this.totalPages);
  }

  toGoBack() {
    this.message= ''
    this.activatedRoute.queryParams.subscribe((params) => {
      const idContrato = params['idContrato'];
      const contrato = params['contrato'];
      const empresa = params['empresa'];
      this.router.navigate([`listar-funcionarios/${idContrato}`], {
        queryParams: {
          idContrato: idContrato,
          contrato: contrato,
          empresa: empresa,
        },
        
      });
      
    });
  }

 
  getCurrentPeriod(): string {
    if (this.selectedPeriod == undefined || this.selectedPeriod == null || this.selectedPeriod.length == 0) {
      const now = new Date();
      const year: number = now.getFullYear();
      const month: number = now.getMonth() + 1;
      const day: number = now.getDate();
  
      return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    } else {
      return this.selectedPeriod;
    }
  }
  

  getMonthName(dateString: string): any {
    const date = new Date(dateString);
    const monthNumber = date.getMonth() + 1;
    const year = date.getFullYear();
  
    const monthNames = [
      'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
      'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
    ];
  
    if (monthNumber >= 1 && monthNumber <= 12) {
      this.periodo =  `${monthNames[monthNumber - 1]}/${year}`;     
    } 
  }

  formatarNumero(numero: number): string {
    const formato = { minimumFractionDigits: 2, maximumFractionDigits: 2 };
    return numero.toLocaleString('pt-BR', formato);
  }


}
