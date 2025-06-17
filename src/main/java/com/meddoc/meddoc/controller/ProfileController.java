package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.model.Exame;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.ConsultaService;
import com.meddoc.meddoc.service.ExameService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ExameService exameService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/idoso/dashboard")
    public String idosoDashboard() {
        return "idoso/dashboard"; // Mapeia para src/main/resources/templates/idoso/dashboard.html
    }

    @GetMapping("/idoso/consultas")
    public String idosoConsultas(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario idoso = usuarioService.findByEmail(userDetails.getUsername());

        List<Consulta> consultas = consultaService.buscarConsultasPorIdoso(idoso.getId());
        List<Exame> exames = exameService.buscarExamesPorIdoso(idoso.getId());

        List<Object> eventos = new ArrayList<>();
        eventos.addAll(consultas.stream().peek(c -> c.setTipo("Consulta")).collect(Collectors.toList()));
        eventos.addAll(exames.stream().peek(e -> e.setTipo("Exame")).collect(Collectors.toList()));

        // Opcional: ordenar por data e hora
        eventos.sort(Comparator.comparing(e -> {
            LocalDate date = null;
            LocalTime time = null;
            if (e instanceof Consulta) {
                Consulta c = (Consulta) e;
                date = c.getData();
                time = c.getHora();
            } else if (e instanceof Exame) {
                Exame ex = (Exame) e;
                date = ex.getData();
                time = ex.getHora();
            }
            if (date == null || time == null) {
                return LocalDateTime.MAX; 
            }
            return date.atTime(time);
        }));

        model.addAttribute("eventos", eventos);
        return "idoso/consultas-idoso";
    }

    @GetMapping("/idoso/chat")
    public String idosoChat() {
        return "idoso/chat";
    }

    @GetMapping("/idoso/medicamentos/form")
    public String idosoMedicamentosForm() {
        return "idoso/medicamentos-form";
    }
} 