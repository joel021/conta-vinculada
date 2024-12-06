import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core'

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css'],
})
export class ToastComponent implements OnInit {
  @Input() title!: string;
  @Input() message!: string;
  @Input() customClass!: string; 
  @Output() closeHit = new EventEmitter<boolean>();
    
  constructor() {}

  ngOnInit(): void {
   
  }
}
