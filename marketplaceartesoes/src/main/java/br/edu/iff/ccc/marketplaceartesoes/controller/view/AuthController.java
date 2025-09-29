package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.AutenticacaoException;
import br.edu.iff.ccc.marketplaceartesoes.service.ArtesaoService;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final ClienteService clienteService;
    private final ArtesaoService artesaoService;

    @Autowired
    public AuthController(ClienteService clienteService, ArtesaoService artesaoService) {
        this.clienteService = clienteService;
        this.artesaoService = artesaoService;
    }

    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String email,
                             @RequestParam String senha,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            // Tenta fazer login como Cliente
            ClienteDTO clienteLogado = clienteService.fazerLogin(email, senha);
            session.setAttribute("usuarioLogado", clienteLogado);
            session.setAttribute("usuarioLogadoId", clienteLogado.id());
            session.setAttribute("tipoUsuario", "CLIENTE");
            return "redirect:/clientes/area-cliente";

        } catch (AutenticacaoException e) {
            // Se o login de cliente falhou, tenta como Artesão
            try {
                ArtesaoDTO artesaoLogado = artesaoService.fazerLogin(email, senha);
                session.setAttribute("usuarioLogado", artesaoLogado);
                session.setAttribute("tipoUsuario", "ARTESAO");
                return "redirect:/artesao/dashboard";
            } catch (AutenticacaoException e2) {
                // Se ambos falharam, exibe a mensagem de erro.
                redirectAttributes.addFlashAttribute("mensagemErro", "E-mail ou senha inválidos.");
                return "redirect:/auth/login";
            }
        }
    }

    @GetMapping("/logout")
    public String fazerLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Você saiu da sua conta.");
        return "redirect:/auth/login";
    }
}