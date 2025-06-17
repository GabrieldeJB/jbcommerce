package com.meddoc.meddoc.repository;

import com.meddoc.meddoc.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByMedico(com.meddoc.meddoc.model.Usuario medico);

    // Novo método para buscar consultas por médico e status
    List<Consulta> findByMedicoAndStatus(com.meddoc.meddoc.model.Usuario medico, String status);

    // Novo método para buscar consultas por ID do paciente idoso
    List<Consulta> findByPacienteIdosoId(Long pacienteIdosoId);
} 