package com.meddoc.meddoc.service;

import com.meddoc.meddoc.model.Medicamento;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.repository.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public void salvar(Medicamento medicamento) {
        medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> findByUsuario(Usuario usuario) {
        return medicamentoRepository.findByUsuario(usuario);
    }

    public Medicamento buscarPorId(Long id) {
        return medicamentoRepository.findById(id).orElse(null);
    }

    public List<Medicamento> obterProximosDoDia(Usuario usuario) {
        // Aqui você pode filtrar por data, horário, etc., conforme sua lógica de negócio
        return medicamentoRepository.findByUsuario(usuario);
    }

    public void removerPorId(Long id) {
        medicamentoRepository.deleteById(id);
    }

    public List<Medicamento> buscarMedicamentos(Usuario usuario, String termoBusca, String tipoFiltro) {
        System.out.println("Buscando medicamentos para o usuário: " + usuario.getEmail());
        System.out.println("Termo de busca: '" + termoBusca + "'");
        System.out.println("Tipo de filtro: '" + tipoFiltro + "'");

        List<Medicamento> resultados;

        if (termoBusca != null && !termoBusca.isEmpty() && tipoFiltro != null && !tipoFiltro.isEmpty()) {
            resultados = medicamentoRepository.findByUsuarioAndNomeContainingIgnoreCaseAndTipoContainingIgnoreCase(usuario, termoBusca, tipoFiltro);
        } else if (termoBusca != null && !termoBusca.isEmpty()) {
            resultados = medicamentoRepository.findByUsuarioAndNomeContainingIgnoreCase(usuario, termoBusca);
        } else if (tipoFiltro != null && !tipoFiltro.isEmpty()) {
            resultados = medicamentoRepository.findByUsuarioAndTipoContainingIgnoreCase(usuario, tipoFiltro);
        } else {
            resultados = medicamentoRepository.findByUsuario(usuario);
        }

        System.out.println("Resultados encontrados: " + resultados.size());
        return resultados;
    }
}

