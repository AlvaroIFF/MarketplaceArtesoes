package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.LojaApiUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.LojaDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.LojaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lojas")
@Tag(name = "Lojas", description = "Endpoint para consulta de informações sobre as lojas dos artesãos")
public class LojaRestController {

    @Autowired
    private LojaService lojaService;

    @GetMapping
    @Operation(summary = "Lista todas as lojas", description = "Retorna uma lista com os detalhes de todas as lojas cadastradas no marketplace.")
    public ResponseEntity<List<LojaDetalheDTO>> buscarTodas() {
        return ResponseEntity.ok(lojaService.buscarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma loja por ID", description = "Retorna os detalhes completos de uma loja específica, incluindo seus produtos.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loja encontrada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Loja não encontrada para o ID informado.", content = @Content)
    })
    public ResponseEntity<LojaDetalheDTO> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lojaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma loja existente", description = "Atualiza os dados de uma loja. Requer que o usuário esteja autenticado como o artesão dono da loja.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Loja atualizada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Loja não encontrada.", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    public ResponseEntity<LojaDetalheDTO> atualizarLoja(@PathVariable Long id, @Valid @RequestBody LojaApiUpdateDTO lojaDto) {
        Long artesaoLogadoId = 1L; 
        LojaDetalheDTO lojaAtualizada = lojaService.atualizarLoja(id, lojaDto, artesaoLogadoId);
        return ResponseEntity.ok(lojaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma loja", description = "Deleta uma loja e, potencialmente, seus produtos. Requer que o usuário seja o artesão dono da loja.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Loja deletada com sucesso."),
        @ApiResponse(responseCode = "404", description = "Loja não encontrada.", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso negado.", content = @Content)
    })
    public ResponseEntity<Void> deletarLoja(@PathVariable Long id) {
        Long artesaoLogadoId = 1L;
        lojaService.deletarLoja(id, artesaoLogadoId);
        return ResponseEntity.noContent().build();
    }
}