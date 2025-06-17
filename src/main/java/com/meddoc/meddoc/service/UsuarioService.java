package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.TipoUsuario;
import com.meddoc.meddoc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmailOrCpf(email, email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
}
