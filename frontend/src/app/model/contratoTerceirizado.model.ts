import { Contrato } from "./contrato.model";
import { PessoaFisica } from "./pessoaFisica.model";
import { PessoaJuridica } from "./pessoaJuridica.model";

export class ContratoTerceirizado{
 id?: number;
 pessoaJuridica?: PessoaJuridica 
 pessoaFisica?: PessoaFisica
 contrato?: Contrato
 contratoTerceirizadoList: any;

 constructor(){
   
 }
   


}



