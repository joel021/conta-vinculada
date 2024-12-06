import { PessoaJuridica } from "./pessoaJuridica.model"


export class Contrato {

    idContrato?: number
    unidade?: string
    inicioVigencia?: string
    fimVigencia?: string
    numero?: string
    status?: string
    pessoaJuridica?: PessoaJuridica
    contratoTerceirizadoList: any
    
    
  
}