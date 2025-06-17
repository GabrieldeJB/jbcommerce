package com.meddoc.meddoc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank(message = "O nome do medicamento é obrigatório")
    private String nome;

    @NotBlank(message = "O horário é obrigatório")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de horário inválido")
    private String horario;

    @NotBlank(message = "A dosagem é obrigatória")
    private String dosagem;

    @NotBlank(message = "A frequência é obrigatória")
    private String frequencia;

    @NotNull(message = "O estoque inicial é obrigatório")
    @Min(value = 0, message = "O estoque inicial não pode ser negativo")
    private Integer estoqueInicial;

    @NotNull(message = "A quantidade de comprimidos por dose é obrigatória")
    @Min(value = 1, message = "A quantidade de comprimidos por dose deve ser maior que zero")
    private Integer comprimidosPorDose;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotBlank(message = "O tipo é obrigatório")
    private String tipo;

    private String proximaDose;

    @NotBlank(message = "A forma farmacêutica é obrigatória")
    private String formaFarmaceutica;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    private String prescritor;

    public Medicamento() {
    }
}
