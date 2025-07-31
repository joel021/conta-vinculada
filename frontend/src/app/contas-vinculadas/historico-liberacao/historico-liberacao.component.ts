import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClientService } from 'src/app/service/http-client.service';
import { Title } from '@angular/platform-browser';
import { ModalCallback, ModalResponse, ModalResultCallBack } from '../shared/modal/modalcallback.class';


@Component({
  selector: 'app-historico-liberacao',
  templateUrl: './historico-liberacao.component.html',
  styleUrls: ['./historico-liberacao.component.css']
})
export class HistoricoLiberacaoComponent implements OnInit, ModalCallback {

 posicaoLiberacao: any = 0
 historicoLiberacao: any;
 form: FormGroup;
 liberacaoFERIAS: any = [] 
 liberacaoFGTS: any = []
 liberacaoDecimoTerceiro: any = []
 loading = false;
 optionsArray: any = []
 errorMessage: string = '';
 message: string = '';
 loaded = false;
 disponivelLiberacao = 0
 somaTotalLiberadoMensal: number | null = null;
 selectedTipo: string | null = null;
 selectedPeriod: string = ''
 checkedItems: any[] = []
 checkboxes: any
 decimoCheckTotal = false
 tipoOptions: string[] = ['DECIMO_TERCEIRO', 'FGTS','FERIAS'];
 _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];
  controls: any;
  pagePosition: number = 0;
  totalPages: number = 1;
  pagesList: number[] = [0];
  arrayDecimoProvisions : any
  get toasts() {
   return this._toasts.filter((f) => f.isShow);
 }
  constructor(
    private apiService: HttpClientService, 
    private activatedRoute : ActivatedRoute,
    private titleService:Title,
    private formBuilder: FormBuilder,  private router: Router) {
      this.titleService.setTitle("Liberações")
      this.form = this.formBuilder.group({
        tipo: ['', [Validators.required]],
        data: ['', [Validators.required]],
        quantidadeLiberacao: ['', [Validators.required]],
      })
    }
  
  
  ngOnInit(): void {
   this.activatedRoute.params.subscribe((params) => {
      const idContrato = this.activatedRoute.snapshot.params['idContrato']
      this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {
        this.historicoLiberacao = resp;  
        this.somaTotalLiberadoMensal = 0            
      },
      error:(erro) =>{        
      },
    })
  });
  }
  
  onResultUserSelect(response: ModalResponse): void {     
    const oficioMovimentacao = {
      docSei : response.docSEI,
      numeroOficio : response.numeroOficio,
      anoOficio : response.anoOficio
    }
    const dataLiberacao = response.dataLiberacao
     if (response.selected == ModalResultCallBack.yes){
     this.createLiberacao(oficioMovimentacao , dataLiberacao)      
    } else {
      this.message = ''
      this.errorMessage = ''
    }
    
  }
 
  listarHistLiberacao() {
    if(this.selectedTipo === 'DECIMO_TERCEIRO' ){
      this.liberacoesDecimoTerceiro()
    }else if (this.selectedTipo === 'FERIAS'){
      this.liberacoesFerias()
    }else{
      this.liberacoesFGTS()
    }    
  }

  liberacoesFGTS(){
    const idContrato = this.activatedRoute.snapshot.params['idContrato']
    const fechamento = this.form.get('data')?.value
    this.apiService.buscarLiberacaoFGTS(idContrato,fechamento).subscribe({
      next:(resp) => {        
        this.liberacaoFERIAS = []
        this.liberacaoDecimoTerceiro = []
        this.liberacaoFGTS = resp
        this.optionsArray = []
        this.somaTotalLiberadoMensal = this.liberacaoFGTS.funcionarioProvisions.reduce((total: any, provisao: any) => {
        return total + provisao.totalProvision; }, 0);
        this.formatarNumero(this.somaTotalLiberadoMensal)
        this.calcularAliberar() 
        if(this.liberacaoFGTS.funcionarioProvisions.length === 0) {
          this.message =
            'Não possui funcionários com liberação de FGTS';
        }
        this.checkboxes = document.querySelectorAll('input[type="checkbox"]:not([disabled])');
    this.checkboxes.forEach((checkbox: any) => {
        checkbox.checked = false;
    });
    this.checkedItems = []
        
      },
      error:(erro) =>{
        this.errorMessage = erro.error.error
      }
    });
  }
  liberacoesFerias(){
    const idContrato = this.activatedRoute.snapshot.params['idContrato']
    const fechamento = this.form.get('data')?.value
    this.apiService.buscarLiberacaoFERIAS(idContrato,fechamento).subscribe({
      next:(resp) => {       
        this.posicaoLiberacao = 0
        this.calculateOptionsArray(resp)
        this.liberacaoFERIAS = resp
        this.liberacaoFGTS = []
        this.liberacaoDecimoTerceiro = []
        this.somaTotalLiberadoMensal = this.liberacaoFERIAS.feriasPeriodProvisionList
        .reduce((total: any, provisao: { feriasProvisionList?: { totalProvision: any; }[]; }) => {
          if (provisao.feriasProvisionList && provisao.feriasProvisionList[this.posicaoLiberacao]) {
                return total + provisao.feriasProvisionList[this.posicaoLiberacao].totalProvision;
            } else {
                return total;
            }
        }, 0);   
        this.formatarNumero(this.somaTotalLiberadoMensal)
        this.calcularAliberar() 
        if(this.liberacaoFERIAS.feriasPeriodProvisionList.length === 0) {
          this.message =
            'Não possui funcionários com liberação de férias';
        }
        this.checkboxes = document.querySelectorAll('input[type="checkbox"]:not([disabled])');
        this.checkboxes.forEach((checkbox: any) => {
        checkbox.checked = false;
         });
        this.checkedItems = [] 
      },
      error:(erro) =>{
        this.errorMessage = erro.error.error
      }
    });
  }

  liberacoesDecimoTerceiro(){
    const idContrato = this.activatedRoute.snapshot.params['idContrato']
    const fechamento = this.form.get('data')?.value
    this.apiService.buscarLiberacaoDECIMOTERCEIRO(idContrato,fechamento).subscribe({
      next:(resp) => {
        this.posicaoLiberacao = 0        
        this.liberacaoFERIAS = []
        this.liberacaoFGTS = []
        this.optionsArray = []    
        this.liberacaoDecimoTerceiro = resp    
        this.arrayDecimoProvisions = [];
        for (let i = 0; i < this.liberacaoDecimoTerceiro.funcionarioProvisions.length; i++) {
            const provisions = this.liberacaoDecimoTerceiro.funcionarioProvisions[i];
            const yearProvisions = Object.entries(provisions.yearDecimoProvisions)
                .map(([year, provision]) => ({ year, provision, contratoTerceirizadoId: provisions.contratoTerceirizadoId }));
            yearProvisions.reverse();
            this.arrayDecimoProvisions.push(yearProvisions); 
        }    
      this.somaTotalLiberadoMensal = this.arrayDecimoProvisions.reduce((total: any, provisions: any[]) => {
        if (this.posicaoLiberacao >= 0 && this.posicaoLiberacao < provisions.length) {
            const provision = provisions[this.posicaoLiberacao]?.provision;
            if (provision) {
                total += provision.totalProvision;
            }
        }
        return total;
    }, 0);
        this.formatarNumero(this.somaTotalLiberadoMensal)
        this.calculateOptionsArray(resp)
        this.calcularAliberar()        
        if(this.liberacaoDecimoTerceiro.funcionarioProvisions.length === 0) {
          this.message =
            'Não possui funcionários com liberação de Decímo Terceiro';
        }
        this.checkboxes = document.querySelectorAll('input[type="checkbox"]:not([disabled])');
    this.checkboxes.forEach((checkbox: any) => {
        checkbox.checked = false;
    });
    this.checkedItems = []
        
        
      },
      error:(erro) =>{
        this.errorMessage = erro.error.error
      }
    });
  }

  onTipoChange(selectedValue: string): void {
    this.selectedTipo = selectedValue;   
  }

  formatarNumero(numero: any): string {
    if (numero == null) {
        return ''; 
    }
    const formato = { minimumFractionDigits: 2, maximumFractionDigits: 2 };
    return numero.toLocaleString('pt-BR', formato);
}

  clearMessage(){
    this.message = ''
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

  calcularAliberar(){
    
    if(this.selectedTipo == 'FERIAS'){
      this.disponivelLiberacao = this.liberacaoFERIAS.totalProvision - this.liberacaoFERIAS.totalLiberation
    }else if(this.selectedTipo == 'FGTS'){
      this.disponivelLiberacao = this.liberacaoFGTS.totalProvision - this.liberacaoFGTS.totalLiberation
    }else if(this.selectedTipo == 'DECIMO_TERCEIRO'){
      this.disponivelLiberacao = this.liberacaoDecimoTerceiro.totalProvision - this.liberacaoDecimoTerceiro.totalLiberation
    }
  }

  calculateOptionsArray(qtdLiberacao: any) {
    let maxProvisionListLength = 0;    
    if(this.selectedTipo == 'FERIAS'){
      for (const funcionario of qtdLiberacao.feriasPeriodProvisionList
        ) {
        const length = funcionario.feriasProvisionList.length;      
        if (length > maxProvisionListLength) {
          maxProvisionListLength = length;         
        }
      }    
    }else if(this.selectedTipo == 'DECIMO_TERCEIRO'){
      for (var i = 0; i < qtdLiberacao.funcionarioProvisions.length; i++ ) {
       const length = this.arrayDecimoProvisions[i].length;      
        if (length > maxProvisionListLength) {
          maxProvisionListLength = length;
          
        }
    }
  }
      this.optionsArray = Array.from({ length: maxProvisionListLength }, (_, i) => i + 1);
      this.form.get('quantidadeLiberacao')?.setValue(this.optionsArray[maxProvisionListLength - 1]);
     
   
    
  }

  qtdLiberacao(optionsArray: number[]) {
    const selectedIndex = this.form.get('quantidadeLiberacao')?.value - 1;
    const ultimoIndice = optionsArray.length - 1;
    this.posicaoLiberacao = ultimoIndice - selectedIndex;
    this.recalcularSomaTotal()
    this.checkboxes = document.querySelectorAll('input[type="checkbox"]:not([disabled])');
    this.checkboxes.forEach((checkbox: any) => {
        checkbox.checked = false;
    });
    this.checkedItems = []
    
    
}
recalcularSomaTotal() {
  if (this.selectedTipo == 'FERIAS') {
    this.somaTotalLiberadoMensal = this.liberacaoFERIAS.feriasPeriodProvisionList.reduce((total: any, provisao: { feriasProvisionList?: { totalProvision: any; }[]; }) => {
      if (provisao.feriasProvisionList && provisao.feriasProvisionList.length > 0) {
        return total + (provisao.feriasProvisionList[this.posicaoLiberacao]?.totalProvision || 0);
      } else {
        return total;
      }
    }, 0);
  } else if (this.selectedTipo == 'DECIMO_TERCEIRO') {   
    this.somaTotalLiberadoMensal = this.arrayDecimoProvisions.reduce((total: any, provisionSet: any[]) => {
      if (this.posicaoLiberacao >= 0 && this.posicaoLiberacao < provisionSet.length) {
          const provision = provisionSet[this.posicaoLiberacao]?.provision;
          if (provision) {              
              total += provision.totalProvision;
          }
      }
      return total;
  }, 0);
}
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

  atualizarPagina(){
    window.location.reload()
  }

  marcarTodos(event: any) {
    this.checkboxes = document.querySelectorAll('input[type="checkbox"]:not([disabled])');
    this.checkboxes.forEach((checkbox: any) => {
      checkbox.checked = event.target.checked;        
      if(checkbox.checked === true  )  {
        this.checkedItems = []  
        this.decimoCheckTotal = true
      }else{
        this.decimoCheckTotal = false
      }
    });      
    
    if (this.selectedTipo === 'DECIMO_TERCEIRO') {      
      for (var i = 0; i < this.arrayDecimoProvisions.length; i++) {
        const item = this.arrayDecimoProvisions[i];
        if (item && item[this.posicaoLiberacao]?.provision && !item[this.posicaoLiberacao].provision.dataLiberacao) {
          this.toggleItem(item);          
          
        }
      }
    }   
    else if (this.selectedTipo === 'FERIAS'){
      for (var i = 0; i < this.liberacaoFERIAS.feriasPeriodProvisionList.length; i++) {
        const item = this.liberacaoFERIAS.feriasPeriodProvisionList[i];
        if (!item.feriasProvisionList[this.posicaoLiberacao]?.dataLiberacao) {
          this.toggleItem(item);        
        }   
      }
    }else{   
      for (var i = 0; i < this.liberacaoFGTS.funcionarioProvisions.length; i++) {
        const item = this.liberacaoFGTS.funcionarioProvisions[i];
        if (!item.dataLiberacao) {
          this.toggleItem(item);        
        }   
      }
    } 
  }
  
  toggleItem(item: any) {    
    const index = this.checkedItems.findIndex((checkedItem: any) => checkedItem === item);
    if (index !== -1) {
        this.checkedItems.splice(index, 1);
    } else {
        item.isChecked = true; 
        this.checkedItems.push(item);
    }
    
}
  getTotalCheckedItems() {
    
    if(this.selectedTipo === 'DECIMO_TERCEIRO' ){      
      return this.checkedItems.reduce((total: number, item: any) => {       
      if(this.decimoCheckTotal ==  true){        
        if (item.isChecked) {           
          return total + (item[this.posicaoLiberacao]?.provision.totalProvision || 0);
        } else {
            return total;            
        }
        
      }else{
        if (item.isChecked) {           
            return total + (item?.provision.totalProvision || 0);
        } else {
            return total;            
        }
      }
        
    } , 0);
    }else if (this.selectedTipo === 'FERIAS'){     
      return this.checkedItems.reduce((total: number, item: any) => {
        if (item.isChecked) {  
            return total + (item.feriasProvisionList[this.posicaoLiberacao]?.totalProvision || 0);
        } else {
            return total;            
        }
    }, 0);
    }else{      
      if(this.checkedItems.length> 0){               
      for (var i = 0; i < this.liberacaoFGTS.funcionarioProvisions.length; i++) {
        return this.checkedItems.reduce((total: number, item: any) => {
          if (item.isChecked) { 
              return total + (item.totalProvision || 0);
          } else {
              return total;            
          }
      }, 0);
    }
  }
  }  

}
//Liberação 

createLiberacao(oficioMovimentacao : any , dataLiberacao: any) {  
    for (var i = 0; i < this.checkedItems.length; i++) {    
    const idContratoTercerizado = this.checkedItems[i].contratoTerceirizadoId
    const contratoTerceirizado = { id: idContratoTercerizado }
    const novaLiberacao = {
      contratoTerceirizado: contratoTerceirizado, 
      tipo: this.form.get('tipo')!.value,
      dataLiberacao : dataLiberacao,  
      oficioMovimentacao : oficioMovimentacao       
  } 
  this.apiService.save(novaLiberacao).subscribe({
    next: () => {    
      this.message = 'Liberação cadastrada com sucesso.'        
      this.atualizarPagina()       
      
    },
    error: (errorObject) => {
     
    }
  });
}


}
}
