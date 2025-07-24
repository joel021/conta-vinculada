package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="inc_grupo_a_contrato")
public class IncGrupoAContrato implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @NotNull(message = "A data de início de vigência da Incidência do Grupo A é obrigatória.")
    public LocalDate data;

    @ManyToOne
    @JoinColumn(name="id_contrato", nullable=false)
    @NotNull(message = "Grupo A contrato deve estar associado a um contrato.")
    public Contrato contrato;

    @Column(name = "inc_grupo_a", precision = 19, scale = 10)
    @NotNull(message = "Você deve fornecer o valor do grupo A.")
    public BigDecimal incGrupoA;

    @ManyToOne
    @JoinColumn(name="id_criado_por")
    public Usuario criadoPor;

    public Calendar criadoEm;

    @ManyToOne
    @JoinColumn(name="id_deletado_por")
    public Usuario deletadoPor;

    public Calendar deletadoEm;
}
