package br.jus.trf1.sjba.contavinculada.core.persistence.model;

import br.jus.trf1.sjba.contavinculada.security.model.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "funcionario")
public class Funcionario implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer idFuncionario;

    @ManyToOne
    @JoinColumn(name = "id_pessoa")
    public PessoaFisica pessoaFisica;

    @Size(min = 4, max = 255, message = "A matrícula deve ter no mínimo 4 caracteres e no máximo 255.")
    public String matricula;

    public boolean isOficialJustica;

    @Enumerated(EnumType.STRING)
    public NivelEnsino nivel;

    @ManyToOne
    @JoinColumn(name="id_criado_por")
    public Usuario criadoPor;

    public Calendar criadoEm;

    @ManyToOne
    @JoinColumn(name="id_deletado_por")
    public Usuario deletadoPor;

    public Calendar deletadoEm;

    public String racaCor;
    
}