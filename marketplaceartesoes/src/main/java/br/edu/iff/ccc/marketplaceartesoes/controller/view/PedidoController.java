package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/sucesso")
    public String exibirConfirmacaoPedido(@ModelAttribute("pedidoId") Long pedidoId, Model model) {
        model.addAttribute("numeroPedido", pedidoId);
        return "pedido-sucesso";
    }

    @GetMapping("/{id}")
    public String detalhePedido(@PathVariable("id") Long pedidoId, Model model, HttpSession session) {
        Long clienteLogadoId = (Long) session.getAttribute("usuarioLogadoId");
        System.out.println(">> Acessando detalhe do pedido: pedidoId=" + pedidoId + ", clienteLogadoId=" + clienteLogadoId);

        if (clienteLogadoId == null) {
            return "redirect:/login";
        }

        PedidoDetalheDTO pedido = pedidoService.buscarPedidoDetalhado(pedidoId, clienteLogadoId);
        System.out.println(">> Pedido encontrado: " + pedido);
        model.addAttribute("pedido", pedido);
        return "detalhe-pedido"; 
    }

    @PostMapping("/{id}/remover")
    public String removerPedido(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long clienteLogadoId = (Long) session.getAttribute("usuarioLogadoId");
        if (clienteLogadoId == null) {
            return "redirect:/auth/login";
        }

        boolean removido = pedidoService.removerPedido(id, clienteLogadoId);
        if (removido) {
            redirectAttributes.addFlashAttribute("sucesso", "Pedido removido com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível remover o pedido.");
        }

        return "redirect:/clientes/area-cliente";
    }
}
