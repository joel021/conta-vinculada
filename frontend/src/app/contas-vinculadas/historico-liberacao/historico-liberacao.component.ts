import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClientService } from 'src/app/service/http-client.service';
import { IliberacaoModel } from '../liberacao/iLiberacao.model';

@Component({
  selector: 'app-historico-liberacao',
  templateUrl: './historico-liberacao.component.html',
  styleUrls: ['./historico-liberacao.component.css']
})
export class HistoricoLiberacaoComponent implements OnInit {
  [x: string]: any;
  //historico: any; 

 // historicoLiberacaos:IliberacaoModel[] = [];
 historicoLiberacao: any;

  constructor(private apiService: HttpClientService, private route: ActivatedRoute,  private activatedRoute : ActivatedRoute,) {}

  ngOnInit(): void {
    /*const idLiberacao = this.route.snapshot.params['id']; // estou passando o ID da liberação na rota
    this.apiService.buscarHistoricoLiberacao(idLiberacao).subscribe((historicoLiberacaos) => {
      this.historicoLiberacaos = historicoLiberacaos;
    }); */ 

    this.activatedRoute.params.subscribe((params) => {
      const idContrato = params['idContrato']
      console.log(idContrato)
      this.apiService.getDadosContrato(idContrato).subscribe({
      next:(resp) => {
        console.log(resp)
        this.historicoLiberacao = resp;
     
      },
      error:(erro) =>{
        console.log('test2' + erro)
      },
    })
  })
  }



  carregarHistorico(idContrato: any) {
    //console.log('test')
    this.apiService.buscarHistoricoLiberacao(idContrato).subscribe({
      next:(resp) => {
        console.log(resp)
        this.historicoLiberacao = resp;
      //  console.log(this.dadosContrato)
      },
      error:(erro) =>{
        console.log('test2' + erro)
      }
    });
  }
}
