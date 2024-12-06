package br.jus.trf1.sjba.contavinculada.core.persistence.model;


import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.utils.DateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.Calendar;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Liberacao implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idLiberacao;

    @NotNull(message = "O profissional deve ser fornecido. A associação com o terceirizado não pode ser inválido.")
    @ManyToOne
    @JoinColumn(name="id_contrato_terceirizado", referencedColumnName = "id")
    public ContratoTerceirizado contratoTerceirizado;

    public LocalDate dataLiberacao;

    @NotNull(message = "Defina o tipo da liberação.")
    @Enumerated(EnumType.STRING)
    public TipoLiberacao tipo;

    @ManyToOne
    @JoinColumn(name="id_criado_por")
    public Usuario criadoPor;

    public Calendar criadoEm;

    @ManyToOne
    @JoinColumn(name="id_deletado_por")
    public Usuario deletadoPor;

    public Calendar deletadoEm;

    @ManyToOne
    @JoinColumn(name="doc_sei")
    @NotNull(message = "Não é possível registrar uma liberação sem o Ofício de Moviemntação.")
    public OficioMovimentacao oficioMovimentacao;

    public int compare(Liberacao l2) {

        if (dataLiberacao == null || l2 == null || l2.getDataLiberacao() == null) {
            return 0;
        }

        if (DateUtils.equalsOrAfter(dataLiberacao, l2.getDataLiberacao())) {
            return 1;
        } else {
            return -1;
        }
    }
}
