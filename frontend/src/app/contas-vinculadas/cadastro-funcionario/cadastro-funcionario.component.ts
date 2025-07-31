import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ListarFuncionariosService } from 'src/app/service/listar-funcionarios.service';

@Component({
  selector: 'app-cadastro-funcionario',
  templateUrl: './cadastro-funcionario.component.html',
  styleUrls: ['./cadastro-funcionario.component.css']
})
export class CadastroFuncionarioComponent implements OnInit {
  form: FormGroup;
  loading = false;
  errorMessage: string = '';
  message: string = '';
  loaded = false;
  funcionarioEncontrado: any
  isDisabled: boolean = true;
  selectedPeriod : any
  verificarCadastro = false
  verificarEditar = false
  _toasts: Array<any> = [{ title: 'Status', message: '', isShow: true }];
  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }
  constructor(
    private funcionario: ListarFuncionariosService, 
    private activatedRoute : ActivatedRoute,
    private formBuilder: FormBuilder,
    private titleService:Title,
    private router: Router) {      
      this.form = this.formBuilder.group({
        cpfTrab: ['',],
        nmTrab: ['', [Validators.required]],
        raca: ['', [Validators.required]],
        nivelEnsino: ['', [Validators.required]],
        cargoTrab: ['', [Validators.required]],
        remunTrab: ['', [Validators.required]],
        dtInicio: ['', [Validators.required]],
        lotacao: ['', [Validators.required]],
        
      })
    }
    get controls() {
      return this.form.controls;
    }

    ngOnInit(): void {
      this.activatedRoute.queryParams.subscribe(params => {
        if(params['message'] !=null){
          this.message= params['message']
        }
        if(params['idFuncionario'] != null){
          this.verificarEditar = true
          this.titleService.setTitle("Editar funcionário")
          this.pesquisarFuncionario(params['cpf']);
          this.form.patchValue({
            cpfTrab: params['cpf']})    
        }else if (params['cpf'] != null) {
          this.titleService.setTitle("Novo termo aditivo")
          this.verificarCadastro = true
          this.pesquisarFuncionario(params['cpf']);
          this.form.patchValue({
          cpfTrab: params['cpf']})
        }else{
          this.titleService.setTitle("Cadastro de funcionário")
        }
        });
        
    }
    
    clearMessage(){
      this.message = ''
    }

    pesquisarFuncionario(cpf: any){
      this.loading = true;
      this.errorMessage = '';     
      this.funcionario.getFuncionario(cpf).subscribe({
        next: (resp) => {
          this.loading = false;
          this.loaded = true;
          if( resp.length >0 && !this.verificarCadastro && !this.verificarEditar){
          this.form.patchValue({
              nmTrab: resp[0].pessoaFisica.nome,
              raca: resp[0].racaCor ,
              nivelEnsino: resp[0].nivel
              })
            
          }else if(
          resp.length >0 && !this.verificarEditar){
            this.form.patchValue({
              nmTrab: resp[0].pessoaFisica.nome,
              raca: resp[0].racaCor ,
              nivelEnsino: resp[0].nivel
              })}else if(
                resp.length >0 && this.verificarEditar){
                  this.form.patchValue({
                    nmTrab: resp[0].pessoaFisica.nome,
                    raca: resp[0].racaCor ,
                    nivelEnsino: resp[0].nivel ,
                    cargoTrab: ' ',
                    remunTrab: ' ',
                    dtInicio: ' ',
                    lotacao: ' ',
                    })
            }else {  
              this.form.patchValue({  
              nmTrab: '',
              raca: '' ,
              nivelEnsino: ''
            })    
            this.message =
              'CPF: ' + cpf +  ' não cadastrado. Se o CPF estiver correto, preencha os dados abaixo para realizar o cadastro.';
              
          }
          
        },
        error: (error) => {
          this.errorMessage = 'Não foi possível obter os dados do funcionário.';
          this.loading = false;
        },
      });

    }

   editarFuncionario(){    
    this.loading = true;
    this.errorMessage = ''; 
    this.activatedRoute.queryParams.subscribe(params => {
      const idFuncionario = params['idFuncionario']
      const idContrato =  params['idContrato']
      const cpf = this.controls['cpfTrab'].value
      const nome = this.controls['nmTrab'].value
      const pessoaFisica = { nome: nome , cpf: cpf }
      const matricula = 'ba' + cpf
      const racaCor = this.controls['raca'].value
      const nivel = this.controls['nivelEnsino'].value  
      this.funcionario.patchFuncionario(idFuncionario , pessoaFisica,
        matricula,
        nivel,
        racaCor ).subscribe({
        next: (resp) => {         
          this.loading = false;
          this.loaded = true;          
          this.router.navigate(['/listar-funcionarios/idContrato'], {
            queryParams: {
              idContrato: idContrato,
              message: 'Funcionário editado com sucesso.',
            },
          })
        },
        error: (error) => {
          if(!error){
            this.errorMessage = error.error.error
          }else{
          this.errorMessage = 'Não foi possível editar o funcionário, gentileza verificar os dados.'
          }
          this.loading = false;
        },
      });
    });

    }

    cadastrarFuncionario(){      
      this.loading = true;
      this.errorMessage = '';
      const cpf = this.controls['cpfTrab'].value
      const nome = this.controls['nmTrab'].value
      const pessoaFisica = { nome: nome , cpf: cpf }
      const matricula = 'ba' + cpf
      const racaCor = this.controls['raca'].value
      const nivel = this.controls['nivelEnsino'].value
      const funcionario = {pessoaFisica: pessoaFisica , matricula: matricula , racaCor : racaCor , nivel : nivel }
      const idContrato = this.activatedRoute.snapshot.params['idContrato']
      const contrato = { idContrato: idContrato }
      const descricao = this.controls['lotacao'].value 
      const lotacao = { descricao: descricao }      
      const dadosContratoTerceirizado = {
        funcionario: funcionario,
        contrato: contrato,
        cargo: this.controls['cargoTrab'].value,
        remuneracao: this.controls['remunTrab'].value.replace(',', '.')  ,
        cargaHoraria: 44,
        dataInicio: this.controls['dtInicio'].value,
        lotacao: lotacao,
      }
      this.funcionario.postContratoTerceirizado(dadosContratoTerceirizado).subscribe({
        next: (resp) => {
          this.loading = false;
          this.form.reset()
          if(this.verificarCadastro === true){
            this.router.navigate(['/listar-funcionarios/idContrato'], {
              queryParams: {
                idContrato: idContrato,
                message: 'Funcionário Cadastrado com sucesso.',
              },
            })
  
          }
          this.message = 'Funcionário Cadastrado com sucesso'
        },
        error: (error) => {          
          if(error){
            this.errorMessage = error.error.errors[0].defaultMessage
          }else{
          this.errorMessage = 'Não foi possível cadastrar o funcionário, gentileza verificar os dados.'
          }
          this.loading = false;
        },
      });

    }

    verificarInput() {
      const cpf = this.controls['cpfTrab'].value
      if (cpf.length === 11) {
        this.isDisabled = false;
      } else {
        this.isDisabled = true;
      }
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

    toGoBack() {
      this.message = ''
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

}
