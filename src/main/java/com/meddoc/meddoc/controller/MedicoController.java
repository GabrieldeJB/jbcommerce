package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.service.UsuarioService;
import com.meddoc.meddoc.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MedicoController {

    private final UsuarioService usuarioService;
    private final ConsultaService consultaService;

    @Autowired
    public MedicoController(UsuarioService usuarioService, ConsultaService consultaService) {
        this.usuarioService = usuarioService;
        this.consultaService = consultaService;
    }

    @GetMapping("/medico/dashboard")
    public String medicoDashboard() {
        return "medico/dashboard"; // Mapeia para src/main/resources/templates/medico/dashboard.html
    }

    @GetMapping("/medico/perfil")
    public String medicoPerfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // O email é o nome de usuário (principal)
        Usuario medico = usuarioService.findByEmail(email);
        model.addAttribute("medico", medico);

        if (medico.getDataNascimento() != null) {
            int idade = Period.between(medico.getDataNascimento(), LocalDate.now()).getYears();
            model.addAttribute("idade", idade);
        }

        return "medico/perfil";
    }

    @GetMapping("/medico/consultas/marcar")
    public String medicoMarcarConsulta(Model model) {
        List<Consulta> consultas = consultaService.buscarTodasConsultas();
        model.addAttribute("consultas", consultas);
        return "medico/consultas";
    }

    @GetMapping("/medico/chat")
    public String medicoChat() {
        return "medico/chat";
    }

    @GetMapping("/medico/exames/marcar")
    public String medicoExames() {
        return "medico/exames";
    }
} 