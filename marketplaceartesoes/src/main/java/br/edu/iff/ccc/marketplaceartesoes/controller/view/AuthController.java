package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteLoginDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ArtesaoService;
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
    private final ArtesaoService artesaoService;

    @Autowired
    public AuthController(ClienteService clienteService, ArtesaoService artesaoService) {
        this.clienteService = clienteService;
        this.artesaoService = artesaoService;
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
            // Tenta fazer login como CLIENTE
            ClienteDTO clienteLogado = clienteService.fazerLogin(dadosLogin.email(), dadosLogin.senha());
        
            // Se a linha acima não lançou exceção, o login foi um sucesso!
            session.setAttribute("usuarioLogado", clienteLogado);
            session.setAttribute("tipoUsuario", "CLIENTE");
            return "redirect:/clientes/area-cliente";

        } catch (IllegalArgumentException clienteException) {
            // Se o login de cliente FALHOU, entra aqui.
        
            // Tenta fazer login como ARTESÃO
            ArtesaoDTO artesaoLogado = artesaoService.fazerLogin(dadosLogin.email(), dadosLogin.senha());
        
            if (artesaoLogado != null) {
                // Se encontrou um artesão, o login foi um sucesso!
                session.setAttribute("usuarioLogado", artesaoLogado);
                session.setAttribute("tipoUsuario", "ARTESAO");
                return "redirect:/artesao/dashboard";
            }
        
            // Se chegou até aqui, NENHUM login deu certo.
            redirectAttributes.addFlashAttribute("mensagemErro", "E-mail ou senha inválidos.");
            return "redirect:/auth/login";
        }
    }

    // Método para FAZER LOGOUT
    @GetMapping("/logout")
    public String fazerLogout(HttpSession session) {
        session.invalidate(); // Limpa a sessão
        return "redirect:/"; // Redireciona para a home
    }
}