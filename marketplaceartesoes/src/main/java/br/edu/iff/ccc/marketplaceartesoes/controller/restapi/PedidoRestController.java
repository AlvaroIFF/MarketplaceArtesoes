package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class PedidoRestController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;

    @Autowired
    public PedidoRestController(PedidoService pedidoService, ClienteService clienteService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Lista pedidos de um cliente", description = "Retorna todos os pedidos de um cliente específico")
    public ResponseEntity<List<PedidoResumoDTO>> listarPedidos(@RequestParam Long clienteId) {
        List<PedidoResumoDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca detalhes de um pedido", description = "Retorna os detalhes completos de um pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoDetalheDTO> buscarPedido(@PathVariable Long id, @RequestParam Long clienteId) {
        PedidoDetalheDTO pedido = pedidoService.buscarPedidoDetalhado(id, clienteId);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Cria um pedido para o cliente especificado com base no carrinho")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio violada")
    })
    public ResponseEntity<PedidoDetalheDTO> criarPedido(@RequestParam Long clienteId, @Valid @RequestBody CarrinhoDTO carrinho) {
        ClienteDTO clienteDto = clienteService.buscarClienteDTOPorId(clienteId);

        // Cria o pedido
        var pedido = pedidoService.criarPedido(carrinho, clienteDto);

        // Gera URI do recurso criado
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.getId())
                .toUri();

        return ResponseEntity.created(location).body(new PedidoDetalheDTO(pedido));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um pedido", description = "Remove um pedido específico pertencente ao cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado ou não pertence ao cliente")
    })
    public ResponseEntity<Void> removerPedido(@PathVariable Long id, @RequestParam Long clienteId) {
        pedidoService.removerPedido(id, clienteId);
        return ResponseEntity.noContent().build();
    }
}
