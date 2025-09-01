package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes") // Todas as URLs aqui começarão com /clientes
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    @Autowired
    public ClienteController(ClienteService clienteService, PedidoService pedidoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }

    // Método para MOSTRAR a página de cadastro
    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "cadastro-cliente";
    }

    // Método para PROCESSAR o envio do formulário de cadastro
    @PostMapping("/cadastrar")
    public String cadastrarCliente(ClienteCadastroDTO dadosCadastro, RedirectAttributes redirectAttributes) {
        try {
            clienteService.cadastrarCliente(dadosCadastro);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça o login.");
            return "redirect:/auth/login"; // Redireciona para a rota CORRETA de login
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/clientes/cadastro";
        }
    }

    // Método para a ÁREA DO CLIENTE
    @GetMapping("/area-cliente")
public String exibirAreaCliente(HttpSession session, Model model) {
    Object usuarioLogado = session.getAttribute("usuarioLogado");
    String tipoUsuario = (String) session.getAttribute("tipoUsuario");

    if (usuarioLogado == null || !"CLIENTE".equals(tipoUsuario)) {
        return "redirect:/auth/login";
    }
        ClienteDTO clienteDto = (ClienteDTO) usuarioLogado;
    
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteDto.id());
    
        model.addAttribute("cliente", clienteDto);
        model.addAttribute("pedidos", pedidos);

        return "area-cliente";
    }
}