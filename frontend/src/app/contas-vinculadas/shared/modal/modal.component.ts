import { Component, Input, TemplateRef, OnInit } from '@angular/core';
import { ModalCallback, ModalResultCallBack } from './modalcallback.class';

import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  
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
  constructor(private modalService: BsModalService) {}


  ngOnInit(): void {
    
  }
 
  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }


  yesClick() {
    this.modalService.hide()
    if(this.context!=null){
      this.context.onResultUserSelect( {selected: ModalResultCallBack.yes, position: this.id})
    }
  }

  noClick() {
    this.modalService.hide()
    if(this.context!=null){
      this.context.onResultUserSelect({selected: ModalResultCallBack.no, position: this.id})
    }
  }
}
