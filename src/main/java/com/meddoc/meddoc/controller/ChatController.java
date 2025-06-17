package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.ChatMessage;
import com.meddoc.meddoc.model.TipoUsuario;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.ChatService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/chat")
    public String exibirChat() { return "idoso/chat"; }

    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    @GetMapping("/api/chat/medico/{idosoId}")
    public List<ChatMessage> getMessagesForMedico(@PathVariable Long idosoId, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario medico = usuarioService.findByEmail(userDetails.getUsername());
        return chatService.getMessagesBetweenUsers(medico.getId(), idosoId);
    }

    @GetMapping("/api/chat/idoso/{medicoId}")
    public List<ChatMessage> getMessagesForIdoso(@PathVariable Long medicoId, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario idoso = usuarioService.findByEmail(userDetails.getUsername());
        return chatService.getMessagesBetweenUsers(idoso.getId(), medicoId);
    }

    @GetMapping("/api/chat/idosos")
    public List<Usuario> getMedicoChats(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario medico = usuarioService.findByEmail(userDetails.getUsername());
        return usuarioService.findByTipoUsuario(TipoUsuario.IDOSO);
    }

    @GetMapping("/api/chat/medicos")
    public List<Usuario> getIdosoChats(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario idoso = usuarioService.findByEmail(userDetails.getUsername());
        return usuarioService.findByTipoUsuario(TipoUsuario.MEDICO);
    }
}
