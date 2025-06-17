package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PerfilController {

    private static final Logger logger = LoggerFactory.getLogger(PerfilController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/idoso/perfil")
    public String mostrarPerfil(Model model, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in PerfilController.mostrarPerfil.");
            
            return "redirect:/login"; 
        }

        String userEmail = principal.getName();
        logger.info("Attempting to fetch user with email: {}", userEmail);

        Usuario usuario = usuarioService.findByEmail(userEmail);

        if (usuario == null) {
            logger.error("User with email {} not found in PerfilController.mostrarPerfil.", userEmail);
            
            return "redirect:/error"; 
        }

        model.addAttribute("usuario", usuario);

        // Calcular idade a partir da data de nascimento
        if (usuario.getDataNascimento() != null) {
            int idade = Period.between(usuario.getDataNascimento(), LocalDate.now()).getYears();
            model.addAttribute("idade", idade);
        } else {
            model.addAttribute("idade", null);
        }

        return "idoso/perfil";
    }
}
