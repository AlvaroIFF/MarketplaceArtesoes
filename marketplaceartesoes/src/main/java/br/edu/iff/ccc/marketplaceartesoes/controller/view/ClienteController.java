package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO; // <-- Adicionado
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ClienteNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService; // <-- Adicionado
import jakarta.servlet.http.HttpSession;
import java.util.List; // <-- Adicionado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService; // <-- Injetar PedidoService

    @Autowired
    public ClienteController(ClienteService clienteService, PedidoService pedidoService) { // <-- Adicionar PedidoService ao construtor
        this.clienteService = clienteService;
        this.pedidoService = pedidoService; // <-- Inicializar PedidoService
    }

    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("clienteCadastroDTO", new ClienteCadastroDTO("", "", null, "", "", "", ""));
        return "cadastro-cliente";
    }

    @PostMapping("/cadastrar")
    public String processarCadastro(@ModelAttribute ClienteCadastroDTO dados, @RequestParam("foto") MultipartFile foto, RedirectAttributes redirectAttributes) {
        try {
            clienteService.cadastrarCliente(dados, foto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Faça login.");
            return "redirect:/auth/login";
        } catch (RegraDeNegocioException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/clientes/cadastro";
        }
    }

    @GetMapping("/area-cliente")
    public String areaCliente(HttpSession session, Model model) {
        ClienteDTO clienteLogado = (ClienteDTO) session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        if (clienteLogado == null || !"CLIENTE".equals(tipoUsuario)) {
            return "redirect:/auth/login";
        }
        
        // --- Adicionado para resolver o erro "pedidos.isEmpty()" ---
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteLogado.id());
        model.addAttribute("pedidos", pedidos);
        // -----------------------------------------------------------

        model.addAttribute("cliente", clienteLogado);
        return "area-cliente";
    }

    @GetMapping("/editar")
    public String exibirFormularioEdicao(HttpSession session, Model model) {
        ClienteDTO clienteLogado = (ClienteDTO) session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        if (clienteLogado == null || !"CLIENTE".equals(tipoUsuario)) {
            return "redirect:/auth/login";
        }

        Cliente clienteEntidade = clienteService.buscarEntidadePorId(clienteLogado.id());
        model.addAttribute("clienteUpdateDTO", new ClienteUpdateDTO(clienteEntidade));
        return "editar-cliente";
    }

    @PostMapping("/editar/processar")
    public String processarEdicao(@ModelAttribute ClienteUpdateDTO dadosUpdate, HttpSession session, RedirectAttributes redirectAttributes) {
        ClienteDTO clienteLogado = (ClienteDTO) session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        if (clienteLogado == null || !"CLIENTE".equals(tipoUsuario)) {
            return "redirect:/auth/login";
        }

        try {
            ClienteDTO clienteAtualizado = clienteService.atualizarCliente(clienteLogado.id(), dadosUpdate);
            session.setAttribute("usuarioLogado", clienteAtualizado); // Atualiza a sessão
            redirectAttributes.addFlashAttribute("sucesso", "Seus dados foram atualizados com sucesso!");
            return "redirect:/clientes/area-cliente";
        } catch (RegraDeNegocioException | ClienteNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/clientes/editar";
        }
    }

    @PostMapping("/deletar")
    public String deletarConta(HttpSession session, RedirectAttributes redirectAttributes) {
        ClienteDTO clienteLogado = (ClienteDTO) session.getAttribute("usuarioLogado");

        if (clienteLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            clienteService.deletarCliente(clienteLogado.id());

            session.invalidate();

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Sua conta foi excluída com sucesso.");
            return "redirect:/"; 
        } catch (RegraDeNegocioException e) {
            
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/clientes/area-cliente"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Ocorreu um erro ao tentar excluir sua conta.");
            return "redirect:/clientes/area-cliente";
        }
    }
}