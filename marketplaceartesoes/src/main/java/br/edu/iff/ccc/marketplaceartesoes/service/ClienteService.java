package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClienteService {

    // Lista em memória para simular o banco de dados de clientes
    private static final List<Cliente> clientesEmMemoria = new ArrayList<>();
    // Simulando o auto-incremento do ID do banco
    private static final AtomicLong idContador = new AtomicLong(1);

    /**
     * Cria um novo cliente e o salva na lista em memória.
     */
    public void cadastrarCliente(ClienteCadastroDTO dadosCadastro) {
        // Validação simples (em um projeto real, isso seria mais robusto)
        if (!dadosCadastro.senha().equals(dadosCadastro.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem!");
        }
        if (buscarPorEmail(dadosCadastro.email()) != null) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        // Criando a entidade Cliente a partir do DTO
        Cliente novoCliente = new Cliente(
                dadosCadastro.nome(),
                dadosCadastro.cpf(),
                dadosCadastro.dtNasc(),
                dadosCadastro.numContato(),
                dadosCadastro.email(),
                dadosCadastro.senha(), // Em um projeto real, a senha seria criptografada aqui
                null // fotoUrl inicial é nula
        );

        // Simulando o ID gerado pelo banco
        novoCliente.setId(idContador.getAndIncrement());

        clientesEmMemoria.add(novoCliente);
        System.out.println("Cliente cadastrado com sucesso: " + novoCliente.getNome());
        System.out.println("Total de clientes na memória: " + clientesEmMemoria.size());
    }
    
    /**
     * Tenta autenticar um cliente.
     * @return um ClienteDTO se o login for bem-sucedido.
     * @throws IllegalArgumentException se as credenciais forem inválidas.
     */
    public ClienteDTO fazerLogin(String email, String senha) {
        Cliente cliente = buscarPorEmail(email);

        // Verifica se o cliente existe e se a senha está correta
        if (cliente != null && cliente.getSenha().equals(senha)) {
            // Sucesso! Retorna um DTO com os dados seguros do cliente.
            return converterParaDTO(cliente);
        }

        // Se chegou até aqui, o login falhou.
        throw new IllegalArgumentException("E-mail ou senha inválidos.");
    }

    // Método auxiliar para verificar se o e-mail já existe
    public Cliente buscarPorEmail(String email) {
        return clientesEmMemoria.stream()
                .filter(cliente -> cliente.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Método auxiliar para converter uma Entity Cliente em um ClienteDTO.
     */
    private ClienteDTO converterParaDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDtNasc(),
                cliente.getNumContato(),
                cliente.getEmail(),
                cliente.getFotoUrl()
        );
    }

    /**
     * Busca uma entidade Cliente pelo seu ID.
     * Usado internamente para associar o cliente a um pedido.
     */
    public Cliente buscarEntidadePorId(Long id) {
        return clientesEmMemoria.stream()
                .filter(cliente -> cliente.getId().equals(id))
                .findFirst()
                // Lança uma exceção se o cliente não for encontrado.
                // Isso ajuda a identificar erros caso algo inesperado aconteça.
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para o ID: " + id));
    }
}