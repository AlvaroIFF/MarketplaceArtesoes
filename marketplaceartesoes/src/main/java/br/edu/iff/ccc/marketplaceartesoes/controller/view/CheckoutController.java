package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import br.edu.iff.ccc.marketplaceartesoes.service.CarrinhoService;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;

    @Autowired
    public CheckoutController(CarrinhoService carrinhoService, PedidoService pedidoService) {
        this.carrinhoService = carrinhoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String processarCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 1. Pega o cliente da sessão para verificar se está logado
        ClienteDTO clienteLogado = (ClienteDTO) session.getAttribute("clienteLogado");

        // 2. Se não houver cliente na sessão, redireciona para o login
        if (clienteLogado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Você precisa fazer login para finalizar a compra.");
            return "redirect:/auth/login";
        }
        
        // 3. Se estiver logado, continua o processo
        CarrinhoDTO carrinho = carrinhoService.getCarrinho();
        if (carrinho.itens().isEmpty()) {
            // Não deveria acontecer se o botão só aparece com itens, mas é uma boa verificação
            return "redirect:/carrinho";
        }

        // 4. Cria o pedido
        Pedido novoPedido = pedidoService.criarPedido(carrinho, clienteLogado);

        // 5. Limpa o carrinho da sessão
        carrinhoService.limparCarrinho();

        // 6. Redireciona para uma página de sucesso
        redirectAttributes.addFlashAttribute("pedidoId", novoPedido.getId());
        return "redirect:/pedidos/sucesso";
    }
}