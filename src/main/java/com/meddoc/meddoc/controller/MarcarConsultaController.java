package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.TipoUsuario;
import com.meddoc.meddoc.service.ConsultaService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class MarcarConsultaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/api/idosos")
    @ResponseBody
    public java.util.List<Usuario> listarIdosos() {
        return usuarioService.findByTipoUsuario(TipoUsuario.IDOSO);
    }

    @PostMapping("/medico/consultas/salvar")
    public String salvarNovaConsulta(@ModelAttribute Consulta consulta, Principal principal, @RequestParam("pacienteIdoso") Long pacienteIdosoId) {
        if (principal == null) {
            return "redirect:/login";
        }
        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        Usuario idoso = usuarioService.findById(pacienteIdosoId)
                .orElseThrow(() -> new IllegalArgumentException("Idoso não encontrado com o ID: " + pacienteIdosoId));

        consulta.setMedico(medico);
        consulta.setEnviadoPor(medico.getNome()); // Quem enviou a consulta
        consulta.setPacienteIdosoId(idoso.getId()); // ID do idoso selecionado
        consulta.setStatus("Pendente"); // Status inicial
        consulta.setDataEnvio(LocalDate.now()); // Data de envio

        consultaService.salvar(consulta);
        return "redirect:/medico/consultas/marcar";
    }
} 