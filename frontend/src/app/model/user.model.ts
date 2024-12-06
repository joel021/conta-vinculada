import { Unidade } from "./unidade.model";

export class Usuario {

    idUsuario?: number
    usuario: string = ''
    nome?: string
    email?: string
    token: string = ''
    papeis?: Array<string>
    enabled?: boolean
    senha?: string
    unidade?: Unidade

    constructor() {
        this.token=''
    }
}
