package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoApiDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Endpoint para gerenciamento completo de produtos")
public class ProdutoRestController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista com os detalhes de todos os produtos cadastrados no marketplace.")
    public ResponseEntity<List<ProdutoDetalheDTO>> buscarTodos() {
        return ResponseEntity.ok(produtoService.buscarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto por ID", description = "Retorna os detalhes de um produto específico com base no seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado para o ID informado.", content = @Content)
    })
    public ResponseEntity<ProdutoDetalheDTO> buscarPorId(@PathVariable("id") Long id) {
        ProdutoDetalheDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo produto", description = "Cria um novo produto no sistema. A imagem deve ser tratada em um endpoint futuro.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para o produto.", content = @Content)
    })
    public ResponseEntity<ProdutoDetalheDTO> cadastrarProduto(@Valid @RequestBody ProdutoApiDTO produtoDto) {
        ProdutoDetalheDTO produtoSalvo = produtoService.cadastrarProdutoApi(produtoDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(produtoSalvo.id()).toUri();
        return ResponseEntity.created(location).body(produtoSalvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto existente", description = "Atualiza todos os dados de um produto com base no seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado para o ID informado.", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para atualização.", content = @Content)
    })
    public ResponseEntity<ProdutoDetalheDTO> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoApiDTO produtoDto) {
        ProdutoDetalheDTO produtoAtualizado = produtoService.atualizarProdutoApi(id, produtoDto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um produto", description = "Remove um produto do sistema com base no seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado para o ID informado.", content = @Content)
    })
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        produtoService.excluirProdutoApi(id);
        return ResponseEntity.noContent().build();
    }
}