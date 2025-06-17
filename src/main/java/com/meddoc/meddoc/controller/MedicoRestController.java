package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.TipoUsuario;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MedicoRestController {

    private final UsuarioService usuarioService;

    @Autowired
    public MedicoRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios/me")
    public ResponseEntity<Usuario> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.findByEmail(userDetails.getUsername());
        // Remover a senha antes de enviar para o frontend por segurança
        usuario.setSenha(null);
        usuario.setConfirmarSenha(null);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/medico/perfil/update")
    public ResponseEntity<Usuario> updateMedicoProfile(@RequestBody Usuario updatedMedico,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Usuario medicoExistente = usuarioService.findByEmail(userDetails.getUsername());

        if (medicoExistente == null || medicoExistente.getTipoUsuario() != TipoUsuario.MEDICO) {
            return ResponseEntity.status(403).build(); // Acesso negado ou não é médico
        }

        // Atualizar apenas os campos permitidos para edição de perfil
        medicoExistente.setNome(updatedMedico.getNome());
        medicoExistente.setEmail(updatedMedico.getEmail());
        medicoExistente.setCelular(updatedMedico.getCelular());
        medicoExistente.setCrm(updatedMedico.getCrm()); // Atualizar CRM
        medicoExistente.setEndereco(updatedMedico.getEndereco());
        medicoExistente.setNumero(updatedMedico.getNumero());
        medicoExistente.setBairro(updatedMedico.getBairro());
        medicoExistente.setCidade(updatedMedico.getCidade());

        usuarioService.salvar(medicoExistente);
        medicoExistente.setSenha(null); // Não retornar senha
        medicoExistente.setConfirmarSenha(null); // Não retornar senha
        return ResponseEntity.ok(medicoExistente);
    }
} 