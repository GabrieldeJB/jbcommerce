package com.meddoc.meddoc.repository;

import com.meddoc.meddoc.model.Exame;
import com.meddoc.meddoc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
    List<Exame> findByMedico(Usuario medico);
    List<Exame> findByMedicoAndStatus(Usuario medico, String status);
    List<Exame> findByPacienteIdosoId(Long pacienteIdosoId);
} 