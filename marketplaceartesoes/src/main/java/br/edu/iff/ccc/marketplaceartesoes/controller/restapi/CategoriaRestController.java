package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.CategoriaDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.CategoriaService;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "Endpoint para consulta de categorias de produtos")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Lista todas as categorias", description = "Retorna uma lista com todas as categorias de produtos disponíveis no marketplace.")
    public ResponseEntity<List<CategoriaDTO>> buscarTodas() {
        return ResponseEntity.ok(categoriaService.buscarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria por ID", description = "Retorna os detalhes de uma categoria específica com base no seu ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada para o ID informado.", content = @Content)
    })
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @GetMapping("/{id}/produtos")
    @Operation(summary = "Lista todos os produtos de uma categoria", description = "Retorna uma lista de todos os produtos que pertencem a uma categoria específica.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos da categoria listados com sucesso."),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada para o ID informado.", content = @Content)
    })
    public ResponseEntity<List<ProdutoDetalheDTO>> buscarProdutosPorCategoria(@PathVariable("id") Long id) {
        return ResponseEntity.ok(produtoService.buscarPorCategoriaId(id));
    }
}