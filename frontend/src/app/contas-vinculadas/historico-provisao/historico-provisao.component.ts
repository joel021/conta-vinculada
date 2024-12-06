import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-historico-provisao',
  templateUrl: './historico-provisao.component.html',
  styleUrls: ['./historico-provisao.component.css']
})
export class HistoricoProvisaoComponent {

  form: FormGroup;
  provisaoEncontradaContrato: any[] = []
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
  get toasts() {
    return this._toasts.filter((f) => f.isShow);
  }

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.form = this.initiForm();
  }

  ngOnInit(): void {
    
  }

  initiForm(): FormGroup {
    return this.formBuilder.group({
      selectedPeriod: [this.getCurrentPeriod()],
      selectedQtPag: 10,
    });
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
  }

  is75Tile(pageNumber: number) {
    return pageNumber == Math.trunc(0.75 * this.totalPages);
  }

  toGoBack() {
    this.activatedRoute.queryParams.subscribe((params) => {
      const id = params['id'];
      const contrato = params['contrato'];
      const empresa = params['empresa'];
      this.router.navigate([`listar-funcionarios/${id}`], {
        queryParams: {
          id: id,
          contrato: contrato,
          empresa: empresa,
        },
      });
    });
  }


  getCurrentPeriod(): string {
  if (this.selectedPeriod == undefined || this.selectedPeriod == null ||  this.selectedPeriod.length == 0) {
    const now = new Date();
    const year: number = now.getFullYear();
    const month: number = now.getMonth() + 1;

    return `${year}-${month.toString().padStart(2, '0')}`;
  } else {
    return this.selectedPeriod;
  }
}


}
