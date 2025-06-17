package com.meddoc.meddoc.repository;

import com.meddoc.meddoc.model.Medicamento;
import com.meddoc.meddoc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    List<Medicamento> findByUsuario(Usuario usuario);
    List<Medicamento> findByUsuarioAndNomeContainingIgnoreCaseAndTipoContainingIgnoreCase(Usuario usuario, String nome, String tipo);
    List<Medicamento> findByUsuarioAndNomeContainingIgnoreCase(Usuario usuario, String nome);
    List<Medicamento> findByUsuarioAndTipoContainingIgnoreCase(Usuario usuario, String tipo);
}
