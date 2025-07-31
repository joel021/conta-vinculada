import { Cidade } from "./cidadade.model";
import { SecaoJudiciaria } from "./secaoJudiciaria.models";

export class Unidade {

    idUnidade?: number;
    nomeUnidade?: string;
    siglaUnidade?: string;
    cidade?: Cidade
    secaoJudiciaria?: SecaoJudiciaria
}
