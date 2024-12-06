export interface ModalCallback {

    onResultUserSelect(response: ModalResponse): void;
}

export class ModalResponse {

    selected: ModalResultCallBack
    position: number

    constructor(selected: ModalResultCallBack, position: number) {
        this.selected = selected
        this.position = position
    }
}

export enum ModalResultCallBack {
    yes = 0,
    no = 1,
}