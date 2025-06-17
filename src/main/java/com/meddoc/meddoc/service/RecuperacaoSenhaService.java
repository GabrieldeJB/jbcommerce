package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RecuperacaoSenhaService {

    private final UsuarioService usuarioService;
    private final JavaMailSender mailSender;
    private final Map<String, CodigoRecuperacao> codigosRecuperacao = new ConcurrentHashMap<>();

    @Autowired
    public RecuperacaoSenhaService(UsuarioService usuarioService, JavaMailSender mailSender) {
        this.usuarioService = usuarioService;
        this.mailSender = mailSender;
    }

    public void enviarCodigoRecuperacao(String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        String codigo = gerarCodigo();
        
        // Salvar código com timestamp
        codigosRecuperacao.put(email, new CodigoRecuperacao(codigo, LocalDateTime.now()));
        
        // Enviar email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperação de Senha - MedDoc");
        message.setText("Seu código de recuperação é: " + codigo + "\n\n" +
                       "Este código expira em 15 minutos.\n" +
                       "Se você não solicitou a recuperação de senha, ignore este email.");
        
        mailSender.send(message);
    }

    public boolean verificarCodigo(String email, String codigo) {
        CodigoRecuperacao codigoRecuperacao = codigosRecuperacao.get(email);
        if (codigoRecuperacao == null) {
            return false;
        }

        // Verificar se o código expirou (15 minutos)
        if (LocalDateTime.now().isAfter(codigoRecuperacao.timestamp.plusMinutes(15))) {
            codigosRecuperacao.remove(email);
            return false;
        }

        return codigoRecuperacao.codigo.equals(codigo);
    }

    public void alterarSenha(String email, String novaSenha) {
        Usuario usuario = usuarioService.findByEmail(email);
        usuario.setSenha(novaSenha);
        usuarioService.salvar(usuario);
        codigosRecuperacao.remove(email);
    }

    private String gerarCodigo() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000); // Gera número entre 100000 e 999999
        return String.valueOf(codigo);
    }

    private static class CodigoRecuperacao {
        final String codigo;
        final LocalDateTime timestamp;

        CodigoRecuperacao(String codigo, LocalDateTime timestamp) {
            this.codigo = codigo;
            this.timestamp = timestamp;
        }
    }
} 