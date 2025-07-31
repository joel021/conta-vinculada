export interface ModalCallback {

    onResultUserSelect(response: ModalResponse): void;
}

export class ModalResponse {

    selected: ModalResultCallBack
    position: number
    docSEI: number
    numeroOficio: number
    anoOficio: number
    dataDesligamento: any
    dataLiberacao: any

    constructor(selected: ModalResultCallBack, position: number, docSEI: number, numeroOficio: number,  anoOficio: number, 
        dataDesligamento: any, dataLiberacao: any ) {
        this.selected = selected
        this.position = position
        this.docSEI = docSEI
        this.numeroOficio = numeroOficio
        this.anoOficio = anoOficio
        this.dataDesligamento = dataDesligamento
        this.dataLiberacao = dataLiberacao
    }
}

export enum ModalResultCallBack {
    yes = 0,
    no = 1,
}