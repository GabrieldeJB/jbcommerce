package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Exame;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.ExameService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class MedicoExameController {

    @Autowired
    private ExameService exameService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/medico/exames")
    public String listarExamesMedico(Model model, Principal principal, @RequestParam(required = false) String status) {
        if (principal == null) {
            return "redirect:/login";
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        String medicoNome = medico != null ? medico.getNome() : "Desconhecido";

        List<Exame> exames = exameService.buscarExamesFiltrados(medico, status);


        model.addAttribute("exames", exames);
        model.addAttribute("medicoNome", medicoNome);
        return "medico/exames";
    }
} 