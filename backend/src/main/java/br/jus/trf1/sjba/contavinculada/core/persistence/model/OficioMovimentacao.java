package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class OficioMovimentacao {

    @Id
    @NotNull(message= "O número do documento sei deve ser informado.")
    public int docSei;

    @NotNull(message= "O número do ofício deve ser informado.")
    public int numeroOficio;

    @NotNull(message = "O ano do ofício deve ser informado.")
    public int anoOficio;
}
