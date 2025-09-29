package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ArtesaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/artesaos")
@Tag(name = "Artesãos", description = "Endpoint para cadastro de novos artesãos e suas lojas")
public class ArtesaoRestController {

    @Autowired
    private ArtesaoService artesaoService;

    @PostMapping
    @Operation(summary = "Cadastra um novo artesão", description = "Cria um novo usuário do tipo artesão e sua respectiva loja no marketplace.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Artesão e loja criados com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/CPF/CNPJ já em uso.", content = @Content)
    })
    public ResponseEntity<ArtesaoApiDTO> cadastrarArtesao(@Valid @RequestBody ArtesaoApiCadastroDTO dadosArtesao) {
        ArtesaoApiDTO artesaoSalvo = artesaoService.cadastrarArtesaoApi(dadosArtesao);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(artesaoSalvo.id())
                .toUri();

        return ResponseEntity.created(location).body(artesaoSalvo);
    }

    // READ (Listar todos - Endpoint de ADMIN)
    @GetMapping
    @Operation(summary = "Lista todos os artesãos (Acesso Restrito)", description = "Retorna uma lista de todos os artesãos. Requer perfil de administrador.")
    public ResponseEntity<List<ArtesaoApiDTO>> buscarTodos() {
        return ResponseEntity.ok(artesaoService.buscarTodosArtesaos());
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    @Operation(summary = "Busca um artesão por ID", description = "Retorna os dados de um artesão e sua loja. Em um sistema real, seria protegido.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artesão encontrado."),
        @ApiResponse(responseCode = "404", description = "Artesão não encontrado.", content = @Content)
    })
    public ResponseEntity<ArtesaoApiDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(artesaoService.buscarArtesaoPorId(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um artesão e sua loja", description = "Atualiza os dados. Requer que o usuário esteja autenticado e seja o dono da conta.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artesão atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Artesão não encontrado.", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já em uso.", content = @Content)
    })
    public ResponseEntity<ArtesaoApiDTO> atualizarArtesao(@PathVariable Long id, @Valid @RequestBody ArtesaoApiUpdateDTO dadosArtesao) {
        ArtesaoApiDTO artesaoAtualizado = artesaoService.atualizarArtesao(id, dadosArtesao);
        return ResponseEntity.ok(artesaoAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um artesão e sua loja", description = "Deleta a conta de um artesão. Requer que o usuário esteja autenticado e seja o dono da conta.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Artesão deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Artesão não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deletarArtesao(@PathVariable Long id) {
        artesaoService.deletarArtesao(id);
        return ResponseEntity.noContent().build();
    }
}