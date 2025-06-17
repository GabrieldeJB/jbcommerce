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
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pacienteIdosoId; // ID do paciente idoso
    private String titulo; // Título do exame
    private String tipoDeExame; // Tipo de exame (e.g., "Ressonância", "Ultrassonografia")
    private String preparacaoNecessaria; // Informações de preparação
    private LocalDate data;
    private LocalTime hora;
    private Integer duracao;
    private String local;
    private String contato;
    private String convenio;
    private String numeroGuia;
    private String nivelUrgencia;
    private String sintomas; // Sintomas relacionados (pode ser útil para exames também)
    private String informacoesPaciente; // Informações adicionais para o paciente
    private String observacoes; // Observações gerais
    private String status; // Status do exame (e.g., "Pendente", "Realizado")
    private String enviadoPor; // Quem enviou o exame (e.g., médico, clínica)
    private LocalDate dataEnvio;
    private boolean notificarPaciente; // Para lembretes
    private Integer lembreteHoras; // Para lembretes
    private String tipo; // Adicionado para identificar se é Consulta ou Exame

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Usuario medico; // Associar o exame a um médico

    // Getters e setters são gerados automaticamente pelo Lombok (@Data)
} 