package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteLoginDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth") // Todas as URLs aqui começarão com /auth
public class AuthController {

    private final ClienteService clienteService;

    @Autowired
    public AuthController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Método para MOSTRAR a página de login
    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "login";
    }

    // Método para PROCESSAR o envio do formulário de login
    @PostMapping("/login")
    public String fazerLogin(ClienteLoginDTO dadosLogin, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            ClienteDTO clienteLogado = clienteService.fazerLogin(dadosLogin.email(), dadosLogin.senha());
            session.setAttribute("clienteLogado", clienteLogado);
            return "redirect:/clientes/area-cliente"; // Sucesso -> Vai para a área do cliente
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/auth/login"; // Erro -> Volta para o login
        }
    }

    // Método para FAZER LOGOUT
    @GetMapping("/logout")
    public String fazerLogout(HttpSession session) {
        session.invalidate(); // Limpa a sessão
        return "redirect:/"; // Redireciona para a home
    }
}