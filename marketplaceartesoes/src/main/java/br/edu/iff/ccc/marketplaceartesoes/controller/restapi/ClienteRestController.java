package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteApiCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteApiDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteApiUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Endpoint para cadastro de novos clientes")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente", description = "Cria um novo usuário do tipo cliente no marketplace.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail/CPF já em uso.", content = @Content)
    })
    public ResponseEntity<ClienteApiDTO> cadastrarCliente(@Valid @RequestBody ClienteApiCadastroDTO dadosCliente) {
        ClienteApiDTO clienteSalvo = clienteService.cadastrarClienteApi(dadosCliente);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.id())
                .toUri();

        return ResponseEntity.created(location).body(clienteSalvo);
    }

    // READ (Listar todos - Endpoint de ADMIN)
    @GetMapping
    @Operation(summary = "Lista todos os clientes (Acesso Restrito)", description = "Retorna uma lista de todos os clientes. Requer perfil de administrador.")
    public ResponseEntity<List<ClienteApiDTO>> buscarTodos() {
        return ResponseEntity.ok(clienteService.buscarTodosClientes());
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente por ID", description = "Retorna os dados de um cliente específico. Em um sistema real, seria protegido.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<ClienteApiDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um cliente", description = "Atualiza os dados de um cliente. Requer que o usuário esteja autenticado e seja o dono da conta.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail já em uso.", content = @Content)
    })
    public ResponseEntity<ClienteApiDTO> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteApiUpdateDTO dadosCliente) {
        ClienteApiDTO clienteAtualizado = clienteService.atualizarCliente(id, dadosCliente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um cliente", description = "Deleta a conta de um cliente. Requer que o usuário esteja autenticado e seja o dono da conta.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}