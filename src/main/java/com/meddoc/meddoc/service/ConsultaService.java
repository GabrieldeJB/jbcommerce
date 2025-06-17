package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.Consulta;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> buscarConsultasPorIdoso(Long idosoId) {
        return consultaRepository.findByPacienteIdosoId(idosoId);
    }

    public List<Consulta> buscarConsultasPorMedico(Usuario medico) {
        return consultaRepository.findByMedico(medico);
    }

    public void salvar(Consulta consulta) {
        consultaRepository.save(consulta);
    }

    public void confirmarPresenca(Long id) {
        // Lógica para confirmar presença
    }

    public void solicitarReagendamento(Long id) {
        // Lógica para solicitar reagendamento
    }

    public void desmarcarConsulta(Long id) {
        // Lógica para desmarcar consulta
    }

    public void ativarNotificacao(Long id) {
        // Lógica para ativar/desativar notificação
    }

    public List<Consulta> buscarTodasConsultas() {
        return consultaRepository.findAll();
    }

    // Novo método para buscar consulta por ID
    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    public void deletar(Long id) {
        consultaRepository.deleteById(id);
    }

    // Novo método para buscar consultas por médico e status
    public List<Consulta> buscarConsultasFiltradas(Usuario medico, String status) {
        if (status != null && !status.isEmpty()) {
            return consultaRepository.findByMedicoAndStatus(medico, status);
        } else {
            return consultaRepository.findByMedico(medico); // Retorna todas as consultas se nenhum filtro for aplicado
        }
    }
}