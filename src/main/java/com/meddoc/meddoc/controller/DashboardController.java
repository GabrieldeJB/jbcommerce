package com.meddoc.meddoc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.model.Medicamento;
import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.service.UsuarioService;
import com.meddoc.meddoc.service.MedicamentoService;
import com.meddoc.meddoc.service.ConsultaService;

import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioService.findByEmail(email);
        List<Medicamento> medicamentos = medicamentoService.obterProximosDoDia(usuario);
        List<Consulta> consultas = consultaService.buscarConsultasPorIdoso(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("medicamentos", medicamentos);
        model.addAttribute("consultas", consultas);

        return "idoso/dashboard";
    }
}
