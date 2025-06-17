package com.meddoc.meddoc.controller;

import com.meddoc.meddoc.model.Usuario;
import com.meddoc.meddoc.service.UsuarioService;
import com.meddoc.meddoc.service.RecuperacaoSenhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final RecuperacaoSenhaService recuperacaoSenhaService;

    @Autowired
    public AuthController(UsuarioService usuarioService, RecuperacaoSenhaService recuperacaoSenhaService) {
        this.usuarioService = usuarioService;
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute Usuario usuario) {
        usuario.setTipoUsuario(usuario.getTipoUsuario());
        usuarioService.salvar(usuario);
        return "redirect:/login";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenha() {
        return "recuperar-senha";
    }

    @PostMapping("/recuperar-senha/enviar-codigo")
    public String enviarCodigoRecuperacao(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            recuperacaoSenhaService.enviarCodigoRecuperacao(email);
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/recuperar-senha/codigo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Email não encontrado ou erro ao enviar código.");
            return "redirect:/recuperar-senha";
        }
    }

    @GetMapping("/recuperar-senha/codigo")
    public String verificarCodigo() {
        return "recuperar-senha-codigo";
    }

    @PostMapping("/recuperar-senha/verificar-codigo")
    public String verificarCodigo(@RequestParam String codigo, 
                                @SessionAttribute("email") String email,
                                RedirectAttributes redirectAttributes) {
        if (recuperacaoSenhaService.verificarCodigo(email, codigo)) {
            return "redirect:/recuperar-senha/nova";
        }
        
        redirectAttributes.addFlashAttribute("erro", "Código inválido ou expirado.");
        return "redirect:/recuperar-senha/codigo";
    }

    @GetMapping("/recuperar-senha/nova")
    public String novaSenha() {
        return "recuperar-senha-nova";
    }

    @PostMapping("/recuperar-senha/salvar-nova-senha")
    public String salvarNovaSenha(@RequestParam String novaSenha,
                                 @SessionAttribute("email") String email,
                                 RedirectAttributes redirectAttributes) {
        try {
            recuperacaoSenhaService.alterarSenha(email, novaSenha);
            redirectAttributes.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao alterar senha.");
            return "redirect:/recuperar-senha/nova";
        }
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
