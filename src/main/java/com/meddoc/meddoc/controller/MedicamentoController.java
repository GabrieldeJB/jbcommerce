package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Medicamento;
import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.MedicamentoService;
import com.meddoc.meddoc.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/idoso/medicamentos")
    public String listarMedicamentos(Model model, Principal principal) {
        Usuario usuario = usuarioService.findByEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        model.addAttribute("medicamentos", medicamentoService.findByUsuario(usuario));
        return "idoso/medicamentos";
    }

    @GetMapping("/idoso/medicamentos/search")
    public String pesquisarMedicamentos(@RequestParam(value = "termoBusca", required = false) String termoBusca,
                                     @RequestParam(value = "tipoFiltro", required = false) String tipoFiltro,
                                     Model model, Principal principal) {
        Usuario usuario = usuarioService.findByEmail(principal.getName());
        List<Medicamento> medicamentos = medicamentoService.buscarMedicamentos(usuario, termoBusca, tipoFiltro);
        model.addAttribute("medicamentos", medicamentos);
        return "idoso/medicamentos :: #medicationListContainer";
    }

    @GetMapping("/idoso/medicamentos/novo")
    public String novoMedicamento(Model model) {
        model.addAttribute("medicamento", new Medicamento());
        return "idoso/medicamentos-form";
    }

    @PostMapping("/idoso/medicamentos")
    public String salvarMedicamento(@Valid @ModelAttribute Medicamento medicamento, 
                                  BindingResult result,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "idoso/medicamentos-form";
        }

        try {
            Usuario usuario = usuarioService.findByEmail(principal.getName());
            medicamento.setUsuario(usuario);
            medicamentoService.salvar(medicamento);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento salvo com sucesso!");
            return "redirect:/idoso/medicamentos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar medicamento: " + e.getMessage());
            return "redirect:/idoso/medicamentos/novo";
        }
    }

    @GetMapping("/idoso/medicamentos/editar/{id}")
    public String editarMedicamento(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Medicamento medicamento = medicamentoService.buscarPorId(id);
            model.addAttribute("medicamento", medicamento);
            return "idoso/medicamentos-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao carregar medicamento: " + e.getMessage());
            return "redirect:/idoso/medicamentos";
        }
    }

    @GetMapping("/idoso/medicamentos/remover/{id}")
    public String removerMedicamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            medicamentoService.removerPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Medicamento removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover medicamento: " + e.getMessage());
        }
        return "redirect:/idoso/medicamentos";
    }

    @GetMapping("/idoso/medicamentos/{id}")
    @ResponseBody
    public Medicamento obterDetalhesMedicamento(@PathVariable Long id) {
        return medicamentoService.buscarPorId(id);
    }
}


