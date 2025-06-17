package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.ConsultaService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/consultas")
    public String listarConsultasIdoso(Model model, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in ConsultaController.listarConsultasIdoso.");
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        logger.info("Attempting to fetch consultations for idoso with email: {}", userEmail);

        Usuario idoso = usuarioService.findByEmail(userEmail);
        List<Consulta> consultas = consultaService.buscarConsultasPorIdoso(idoso.getId());
        
        if (consultas == null) {
            logger.warn("Consultas list is null for idoso {}. Returning empty list.", userEmail);
            consultas = new java.util.ArrayList<>();
        }

        model.addAttribute("consultas", consultas);
        return "idoso/consultas-idoso";
    }

    @GetMapping("/medico/consultas")
    public String listarConsultasMedico(Model model, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in ConsultaController.listarConsultasMedico.");
            return "redirect:/login";
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        String medicoNome = medico != null ? medico.getNome() : "Desconhecido";

        logger.info("Attempting to fetch consultations for medico: {}", medicoNome);

        List<Consulta> consultas = consultaService.buscarConsultasPorMedico(medico);
        
        if (consultas == null) {
            logger.warn("Consultas list is null for medico {}. Returning empty list.", medicoNome);
            consultas = new java.util.ArrayList<>();
        }

        model.addAttribute("consultas", consultas);
        return "medico/consultas";
    }

    @PostMapping("/consultas/{id}/confirmar")
    public String confirmarPresenca(@PathVariable Long id) {
        consultaService.confirmarPresenca(id);
        return "redirect:/idoso/consultas";
    }

    @PostMapping("/consultas/{id}/reagendar")
    public String solicitarReagendamento(@PathVariable Long id) {
        consultaService.solicitarReagendamento(id);
        return "redirect:/idoso/consultas";
    }

    @PostMapping("/consultas/{id}/desmarcar")
    public String desmarcarConsulta(@PathVariable Long id) {
        consultaService.desmarcarConsulta(id);
        return "redirect:/idoso/consultas";
    }

    @PutMapping("/api/consultas/{id}")
    public ResponseEntity<Consulta> atualizarConsulta(@PathVariable Long id, @RequestBody Consulta consulta, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        if (medico == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Consulta> existingConsultaOptional = consultaService.findById(id);
        if (existingConsultaOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Consulta existingConsulta = existingConsultaOptional.get();
        existingConsulta.setPacienteIdosoId(consulta.getPacienteIdosoId());
        existingConsulta.setEspecialidade(consulta.getEspecialidade());
        existingConsulta.setData(consulta.getData());
        existingConsulta.setHora(consulta.getHora());
        existingConsulta.setDuracao(consulta.getDuracao());
        existingConsulta.setLocal(consulta.getLocal());
        existingConsulta.setContato(consulta.getContato());
        existingConsulta.setConvenio(consulta.getConvenio());
        existingConsulta.setNumeroGuia(consulta.getNumeroGuia());
        existingConsulta.setNivelUrgencia(consulta.getNivelUrgencia());
        existingConsulta.setSintomas(consulta.getSintomas());
        existingConsulta.setInformacoesPaciente(consulta.getInformacoesPaciente());
        existingConsulta.setObservacoes(consulta.getObservacoes());
        existingConsulta.setStatus(consulta.getStatus());
        existingConsulta.setEnviadoPor(consulta.getEnviadoPor());
        existingConsulta.setDataEnvio(consulta.getDataEnvio());
        existingConsulta.setNotificarPaciente(consulta.isNotificarPaciente());
        existingConsulta.setLembreteHoras(consulta.getLembreteHoras());
        existingConsulta.setMedico(medico);

        consultaService.salvar(existingConsulta);
        return new ResponseEntity<>(existingConsulta, HttpStatus.OK);
    }

    @DeleteMapping("/api/consultas/{id}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isPresent()) {
            consultaService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/consultas")
    public ResponseEntity<Consulta> criarConsulta(@RequestBody Consulta consulta, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in ConsultaController.criarConsulta.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        if (medico == null) {
            logger.warn("Médico com email {} não encontrado ao criar consulta.", medicoEmail);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        logger.info("Recebida requisição para criar consulta: {}", consulta);
        consulta.setMedico(medico);
        consultaService.salvar(consulta);
        return new ResponseEntity<>(consulta, HttpStatus.CREATED);
    }

    @PostMapping("/consultas/{id}/notificacao")
    public String ativarNotificacao(@PathVariable Long id) {
        consultaService.ativarNotificacao(id);
        return "redirect:/idoso/consultas";
    }

    @GetMapping("/api/consultas/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable Long id) {
        logger.info("Attempting to fetch consulta with ID: {}", id);
        Optional<Consulta> consulta = consultaService.findById(id);
        if (consulta.isPresent()) {
            logger.info("Consulta with ID {} found.", id);
            return new ResponseEntity<>(consulta.get(), HttpStatus.OK);
        } else {
            logger.warn("Consulta with ID {} not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Novo endpoint para buscar consultas filtradas por médico
    @GetMapping("/api/medico/consultas/filtradas")
    public ResponseEntity<List<Consulta>> getFilteredConsultasMedico(
            Principal principal,
            @RequestParam(required = false) String status) {
        if (principal == null) {
            logger.warn("Principal object is null in ConsultaController.getFilteredConsultasMedico.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        String medicoNome = medico != null ? medico.getNome() : "Desconhecido";

        logger.info("Attempting to fetch filtered consultations for medico: {} with status: {}", medicoNome, status);
        List<Consulta> consultas = consultaService.buscarConsultasFiltradas(medico, status);

        return new ResponseEntity<>(consultas, HttpStatus.OK);
    }
}