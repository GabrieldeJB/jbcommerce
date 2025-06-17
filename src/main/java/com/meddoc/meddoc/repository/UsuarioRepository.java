package com.meddoc.meddoc.repository;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailOrCpf(String email, String cpf);
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    Optional<Usuario> findById(Long id);
}