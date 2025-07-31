import { Component, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import { ActivatedRoute, Router } from '@angular/router'
import {  ModalCallback,  ModalResponse,} from '../shared/modal/modalcallback.class'
import { ContratoService } from 'src/app/service/contrato.service'
import { Title } from '@angular/platform-browser'

@Component({
  selector: 'app-inc-grupo-a',
  templateUrl: './inc-grupo-a.component.html',
  styleUrls: ['./inc-grupo-a.component.css'],
})
export class IncGrupoAComponent implements OnInit, ModalCallback {
  form: FormGroup
  message: any = ''
  constructor(
    private formBuilder: FormBuilder,
    private contratService: ContratoService,
    private activatedRoute: ActivatedRoute,
    private titleService:Title,
    private router: Router
  ) {
    this.titleService.setTitle("Incidência Grupo A") 
    this.form = this.formBuilder.group({
      incGrupoA: ['', [Validators.required]],
      data: ['', [Validators.required]],
    })
  }
  onResultUserSelect(response: ModalResponse): void {
    throw new Error('Method not implemented.')
  }

  ngOnInit(): void {}

  get controls() {
    return this.form.controls
  }

  adicionarIncGrupA() {
    const idContrato = this.activatedRoute.snapshot.params['idContrato']
    const contrato = { idContrato: idContrato }
    const dadosIncGrupoA = {
      contrato: contrato,
      incGrupoA: this.controls['incGrupoA'].value,
      data: this.controls['data'].value,
    }
    this.contratService.adicionarIncGrupA(dadosIncGrupoA).subscribe({
      next: (resp) => {
        this.router.navigate(['/listar-funcionarios/idContrato'], {
          queryParams: {
            idContrato: idContrato,
            message: 'Incidência grupo A atualizado com sucesso.',
          },
        })
      },
      error: (error) => {
        
      },
    })
  }

  getCurrentPeriod(): string {
    const now = new Date()
    const year: number = now.getFullYear()
    const month: number = now.getMonth() + 1
    const day: number = now.getDate()

    return `${year}-${month.toString().padStart(2, '0')}-${day
      .toString()
      .padStart(2, '0')}`
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
