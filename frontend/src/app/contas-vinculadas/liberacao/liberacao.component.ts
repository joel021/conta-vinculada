import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClientService } from 'src/app/service/http-client.service';
import { IliberacaoModel } from './iLiberacao.model';
import { ContratoTerceirizado } from 'src/app/model/contratoTerceirizado.model';



@Component({
  selector: 'app-liberacao',
  templateUrl: 'liberacao.component.html',
  styleUrls: ['liberacao.component.css']
})
export class LiberacaoComponent implements OnInit {
  liberacaoForm: FormGroup;
  loading = false
  submitted = false
  errorMessage: string = ''
  liberadoFGTS : any
  liberacaoFGTS = false
  tipoOptions: string[] = ['DECIMO_TERCEIRO', 'FGTS','FERIAS'];
 // valorExibidoSelecionado: string = '';
  dadosContrato: any ;
  isEditing: boolean = false;
  selectedPeriod: string = ''
  selectedTipo: string | null = null;
  selectedTipoDisplay: string | null = null;
  
 

  constructor(private formBuilder: FormBuilder, 
    private http: HttpClient,
    private activatedRoute : ActivatedRoute,
    private apiService: HttpClientService,
    private router: Router) {
   
      this.liberacaoForm = this.formBuilder.group({        
        tipo: [null, Validators.required],
        data: ['']        
       });
    
  }

 

  /*1812 */
  onTipoChange(selectedValue: string): void {
    this.selectedTipo = selectedValue;
    this.selectedTipoDisplay = this.getDisplayValue(selectedValue);
  }

  getDisplayValue(option: string): string {
    if(option == 'FGTS' ){
      this.liberacaoFGTS = true
      this.liberacaoForm = this.formBuilder.group({  
        tipo: [option, Validators.required],      
        data: ['', Validators.required],        
       })      
    }else{
      
      this.liberacaoFGTS = false
    }
    switch (option) {
      case 'DECIMO_TERCEIRO':
        return 'Liberação do valor do Décimo 13°';       
      case 'FGTS':
        return 'Liberação do FGTS';
      case 'FERIAS':
        return 'Liberação das Férias';
      default:
        return 'Nenhum';
    }
  }

  /* */

  ngOnInit(): void {
    
    this.activatedRoute.params.subscribe((params) => {
      const idContrato = params['idContrato']
      this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {       
        this.dadosContrato = resp;
     
      },
      error:(erro) =>{        
      },
    })
  })
}

 
  

  
  carregarDadosContrato(idContrato: any) {
    this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {       
        this.dadosContrato = resp;      
      },
      error:(erro) =>{
        
      }
    });
  }
  

  /* 15122023*/ 

  createLiberacao() { 
    if(this.selectedTipo == 'FGTS'){                 
        this.liberadoFGTS =  this.liberacaoForm.get('data')!.value         
      }else{
        this.liberadoFGTS = ''
      }
      const idContrato = this.activatedRoute.snapshot.params['idContrato']
      const idContratoTercerizado = this.activatedRoute.snapshot.params['idContratoTercerizado']
      const contratoTerceirizado = { id: idContratoTercerizado }
      const novaLiberacao = {
        contratoTerceirizado: contratoTerceirizado,
        tipo: this.liberacaoForm.get('tipo')!.value,        
        dataDesligamento:  this.liberadoFGTS   
    }    
    this.apiService.save(novaLiberacao).subscribe({
      next: () => {        
        this.router.navigate(['/listar-funcionarios/idContrato'], {
          queryParams: {
            idContrato: idContrato,
            message: 'Liberação cadastrada com sucesso.',
          },
        })

      },
      error: (errorObject) => {
               
      }
    });
  }

  updateLiberacao() {}


  

  

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

  

}

  

  

