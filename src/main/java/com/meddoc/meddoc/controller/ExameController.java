package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Exame;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.ExameService;
import com.meddoc.meddoc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController // Usar RestController para APIs REST
@RequestMapping("/api/exames")
public class ExameController {

    private static final Logger logger = LoggerFactory.getLogger(ExameController.class);

    @Autowired
    private ExameService exameService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Exame> criarExame(@RequestBody Exame exame, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in ExameController.criarExame.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        if (medico == null) {
            logger.warn("Médico com email {} não encontrado ao criar exame.", medicoEmail);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        exame.setMedico(medico);

        logger.info("Recebida requisição para criar exame: {}", exame);
        exameService.salvar(exame);
        return new ResponseEntity<>(exame, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exame> getExameById(@PathVariable Long id) {
        logger.info("Attempting to fetch exame with ID: {}", id);
        Optional<Exame> exame = exameService.findById(id);
        if (exame.isPresent()) {
            logger.info("Exame with ID {} found.", id);
            return new ResponseEntity<>(exame.get(), HttpStatus.OK);
        } else {
            logger.warn("Exame with ID {} not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/medico/filtrados")
    public ResponseEntity<List<Exame>> getFilteredExamesMedico(
            Principal principal,
            @RequestParam(required = false) String status) {
        if (principal == null) {
            logger.warn("Principal object is null in ExameController.getFilteredExamesMedico.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        String medicoNome = medico != null ? medico.getNome() : "Desconhecido";

        logger.info("Attempting to fetch filtered exams for medico: {} with status: {}", medicoNome, status);
        List<Exame> exames = exameService.buscarExamesFiltrados(medico, status);

        return new ResponseEntity<>(exames, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exame> atualizarExame(@PathVariable Long id, @RequestBody Exame exame, Principal principal) {
        if (principal == null) {
            logger.warn("Principal object is null in ExameController.atualizarExame.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String medicoEmail = principal.getName();
        Usuario medico = usuarioService.findByEmail(medicoEmail);
        if (medico == null) {
            logger.warn("Médico com email {} não encontrado ao atualizar exame.", medicoEmail);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Exame> existingExameOptional = exameService.findById(id);
        if (existingExameOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Exame existingExame = existingExameOptional.get();
        existingExame.setPacienteIdosoId(exame.getPacienteIdosoId());
        existingExame.setTitulo(exame.getTitulo());
        existingExame.setTipoDeExame(exame.getTipoDeExame());
        existingExame.setPreparacaoNecessaria(exame.getPreparacaoNecessaria());
        existingExame.setData(exame.getData());
        existingExame.setHora(exame.getHora());
        existingExame.setDuracao(exame.getDuracao());
        existingExame.setLocal(exame.getLocal());
        existingExame.setContato(exame.getContato());
        existingExame.setConvenio(exame.getConvenio());
        existingExame.setNumeroGuia(exame.getNumeroGuia());
        existingExame.setNivelUrgencia(exame.getNivelUrgencia());
        existingExame.setSintomas(exame.getSintomas());
        existingExame.setInformacoesPaciente(exame.getInformacoesPaciente());
        existingExame.setObservacoes(exame.getObservacoes());
        existingExame.setStatus(exame.getStatus());
        existingExame.setEnviadoPor(exame.getEnviadoPor());
        existingExame.setDataEnvio(exame.getDataEnvio());
        existingExame.setNotificarPaciente(exame.isNotificarPaciente());
        existingExame.setLembreteHoras(exame.getLembreteHoras());
        existingExame.setMedico(medico);

        exameService.salvar(existingExame);
        return new ResponseEntity<>(existingExame, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarExame(@PathVariable Long id) {
        Optional<Exame> exame = exameService.findById(id);
        if (exame.isPresent()) {
            exameService.deletar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
} 