package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import br.jus.trf1.sjba.contavinculada.utils.DateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ContratoTerceirizado implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @NotNull(message = "Funcionário não fornecido. O contrato deve ser de um funcionário.")
    @ManyToOne
    @JoinColumn(name="id_funcionario", referencedColumnName = "idFuncionario")
    public Funcionario funcionario;

    @NotNull(message = "Deve estar associado a um contrato.")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="id_contrato", referencedColumnName = "idContrato")
    public Contrato contrato;

    public String cargo;

    @Column(precision = 19, scale = 10)
    public BigDecimal remuneracao;

    public int cargaHoraria;

    public LocalDate dataInicio;

    @ManyToOne
    @JoinColumn(name="id_criado_por")
    public Usuario criadoPor;

    public Calendar criadoEm;

    @ManyToOne
    @JoinColumn(name="id_deletado_por")
    public Usuario deletadoPor;

    public Calendar deletadoEm;

    @ManyToOne
    @JoinColumn(name="id_lotacao")
    public Lotacao lotacao;

    public LocalDate dataDesligamento;

    public int compare(ContratoTerceirizado c2) {

        if (dataInicio == null || c2 == null || c2.getDataInicio() == null) {
            return 0;
        }

        if (!DateUtils.equalsOrAfter(dataInicio, c2.getDataInicio())) {
            return -1;
        } else {
            return 1;
        }
    }
    
}
