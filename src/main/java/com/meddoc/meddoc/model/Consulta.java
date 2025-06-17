package com.meddoc.meddoc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Data
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pacienteIdosoId; // ID do paciente idoso
    private String status; // "Pendente", "Confirmada", etc.
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico; // Relacionamento com a entidade Usuario
    private String especialidade;
    private LocalDate data;
    private LocalTime hora;
    private Integer duracao;
    private String local;
    private String contato;
    private String convenio;
    private String numeroGuia;
    private String nivelUrgencia;
    private String sintomas;
    private String informacoesPaciente;
    private String observacoes;
    private String enviadoPor;
    private LocalDate dataEnvio;
    private boolean notificarPaciente; // Para lembretes
    private Integer lembreteHoras; // Para lembretes
    private String tipo; // Adicionado para identificar se é Consulta ou Exame

    // Getters e setters
}