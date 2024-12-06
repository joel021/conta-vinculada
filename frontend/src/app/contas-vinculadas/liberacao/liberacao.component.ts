import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
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
  tipoOptions: string[] = ['DECIMO_TERCEIRO', 'ABONO','FERIAS'];
 // valorExibidoSelecionado: string = '';
  dadosContrato: any ;
  isEditing: boolean = false;

  selectedTipo: string | null = null;
  selectedTipoDisplay: string | null = null;
  
 

  constructor(private formBuilder: FormBuilder, 
    private http: HttpClient,
    private activatedRoute : ActivatedRoute,
    private apiService: HttpClientService,) {
   
      this.liberacaoForm = this.formBuilder.group({
        //tipo: ['']
        tipo: [null, Validators.required]
       
        
      
       });
    
  }

 

  /*1812 */
  onTipoChange(selectedValue: string): void {
    this.selectedTipo = selectedValue;
    this.selectedTipoDisplay = this.getDisplayValue(selectedValue);
  }

  getDisplayValue(option: string): string {
    switch (option) {
      case 'DECIMO_TERCEIRO':
        return 'Liberação do valor do Décimo 13°';
      case 'ABONO':
        return 'Liberação do Abono';
      case 'FERIAS':
        return 'Liberação das Férias';
      default:
        return 'Nenhum';
    }
  }

  /* */

  ngOnInit(): void {

    console.log(this.liberacaoForm);
 //   this.carregarDadosContrato(1272)
    
    this.activatedRoute.params.subscribe((params) => {
      const idContrato = params['idContrato']
      console.log(idContrato)
      this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {
        console.log(resp)
        this.dadosContrato = resp;
     
      },
      error:(erro) =>{
        console.log('test2' + erro)
      },
    })
  })
}

 
  /*salvarValorExibidoSelecionado() {
    const dados = {
      valorExibido: this.valorExibidoSelecionado
    }; 


    // Enviar a solicitação HTTP para a API
    this.http.post('URL_DA_API', dados).subscribe(
      response => {
        console.log('Valor exibido salvo com sucesso!');
      },
      error => {
        console.error('Erro ao salvar o valor exibido:', error);
      }
    );
  } */ 

  
  carregarDadosContrato(idContrato: any) {
    //console.log('test')
    this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {
        console.log(resp)
        this.dadosContrato = resp;
      //  console.log(this.dadosContrato)
      },
      error:(erro) =>{
        console.log('test2' + erro)
      }
    });
  }
  

  /* 15122023*/ 

  createLiberacao() {
    // Criar uma instância do seu modelo e atribuir os valores do formulário
    const novaLiberacao: IliberacaoModel = {
      
      tipo: this.liberacaoForm.get('tipo')!.value,
      //contratoTerceirizado: new ContratoTerceirizado(),
   
      
    };
    this.apiService.save(novaLiberacao).subscribe({
      next: () => {
       // this.findAll();
        //this.liberacaoForm.reset();
        this.onSucess(); /*this.reloadCurrentPage(); */
        

      },
      error: (errorObject) => {
        this.onError();
        console.log(errorObject);
      }
    });
  }

  updateLiberacao() {}


  

  private onSucess() {
   console.log('Liberação cadastrada com sucesso');
  }

  private onError() {
    console.log('Eror ao cadastrar liberacão');
  }

  /* */



/*  tipoOptions = ['DECIMO_TERCEIRO', 'ABONO', 'FERIAS'];
  selectedTipo: string | null = null;
  selectedTipoDisplay: string | null = null;

  onTipoChange(selectedValue: string): void {
    this.selectedTipo = selectedValue;
    this.selectedTipoDisplay = this.getDisplayValue(selectedValue);
  }

  getDisplayValue(option: string): string {
    switch (option) {
      case 'DECIMO_TERCEIRO':
        return 'Décimo 13°';
      case 'ABONO':
        return 'Abono';
      case 'FERIAS':
        return 'Férias';
      default:
        return 'Nenhum';
    }
  }
} */ 

}

  

  

