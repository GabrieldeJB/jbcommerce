package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.Exame;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExameService {

    @Autowired
    private ExameRepository exameRepository;

    public void salvar(Exame exame) {
        exameRepository.save(exame);
    }

    public Optional<Exame> findById(Long id) {
        return exameRepository.findById(id);
    }

    public List<Exame> buscarTodosExames() {
        return exameRepository.findAll();
    }

    public void deletar(Long id) {
        exameRepository.deleteById(id);
    }

    public List<Exame> buscarExamesPorMedico(Usuario medico) {
        return exameRepository.findByMedico(medico);
    }

    public List<Exame> buscarExamesPorIdoso(Long idosoId) {
        return exameRepository.findByPacienteIdosoId(idosoId);
    }

    public List<Exame> buscarExamesFiltrados(Usuario medico, String status) {
        if (status != null && !status.isEmpty()) {
            return exameRepository.findByMedicoAndStatus(medico, status);
        } else {
            return exameRepository.findByMedico(medico);
        }
    }
} 