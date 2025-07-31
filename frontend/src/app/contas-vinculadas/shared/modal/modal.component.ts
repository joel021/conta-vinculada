import { Component, Input, TemplateRef, OnInit } from '@angular/core';
import { ModalCallback, ModalResultCallBack } from './modalcallback.class';

import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  form: FormGroup
  @Input() title!: string
  @Input() body!:string
  @Input() yesButton!:string
  @Input() noButton!:string
  @Input() id!:any
  @Input() buttonTitle:any
  @Input() context!:ModalCallback
  @Input() buttonClass!: string
  @Input() enabled = false

  modalRef?: BsModalRef;
  constructor(private modalService: BsModalService,
    private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      docSEI: ['', [Validators.required]],
      anoOficio: ['', [Validators.required]],
      numeroOficio: ['', [Validators.required]],
      dataDesligamento: ['', [Validators.required]],
      dataLiberacao: ['', [Validators.required]]
    })
  }


  ngOnInit(): void {
    
  }
 
  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }

  get controls() {
    return this.form.controls;
  }


  yesClick() {
    const docSEI = this.controls['docSEI'].value 
    const numeroOficio = this.controls['numeroOficio'].value;
    const anoOficio = this.controls['anoOficio'].value     
    const dataDesligamento =  this.controls['dataDesligamento'].value
    const dataLiberacao = this.controls['dataLiberacao'].value
    this.modalService.hide()
    if(this.context!=null){
      this.context.onResultUserSelect( {selected: ModalResultCallBack.yes, position: this.id, 
        docSEI: docSEI, numeroOficio: numeroOficio , anoOficio: anoOficio, dataDesligamento: dataDesligamento, dataLiberacao : dataLiberacao  })
    }
  }

  noClick() {
    this.modalService.hide()
    if(this.context!=null){
      this.context.onResultUserSelect({selected: ModalResultCallBack.no, position: this.id, 
        docSEI: 0, numeroOficio: 0, anoOficio: 0, dataDesligamento: 0, dataLiberacao:0})
    }
  }
}
