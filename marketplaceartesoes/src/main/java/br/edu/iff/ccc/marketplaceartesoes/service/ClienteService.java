package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ClienteNaoEncontrado;
import br.edu.iff.ccc.marketplaceartesoes.repository.ClienteRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.Optional;

@Service
public class ClienteService {

    // 1. Injetar o repositório em vez de usar a lista em memória
    private final ClienteRepository clienteRepository;

    @Autowired // O Spring injetará o ClienteRepository automaticamente
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // 2. A lista em memória e o contador de ID foram removidos.

    /**
     * Cria um novo cliente e o salva no banco de dados.
     */
    @Transactional // Garante que a operação seja atômica
    public void cadastrarCliente(ClienteCadastroDTO dadosCadastro) {
        // Validação de senha
        if (!dadosCadastro.senha().equals(dadosCadastro.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem!");
        }

        // Verifica se o e-mail já está em uso no banco de dados
        if (clienteRepository.findByEmail(dadosCadastro.email()).isPresent()) {
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

        // O ID será gerado automaticamente pelo JPA ao salvar, não precisamos de idContador.
        clienteRepository.save(novoCliente); // Salva o novo cliente no banco de dados
        System.out.println("Cliente cadastrado com sucesso: " + novoCliente.getNome() + " (ID: " + novoCliente.getId() + ")");
    }
    
    /**
     * Tenta autenticar um cliente.
     * @return um ClienteDTO se o login for bem-sucedido.
     * @throws IllegalArgumentException se as credenciais forem inválidas.
     */
    @Transactional(readOnly = true) // Apenas lê dados
    public ClienteDTO fazerLogin(String email, String senha) {
        // Busca o cliente pelo e-mail no banco
        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(email);

        // Verifica se o cliente existe e se a senha está correta
        // (Em um projeto real, a senha seria comparada com uma versão criptografada)
        if (clienteOptional.isPresent() && clienteOptional.get().getSenha().equals(senha)) {
            // Sucesso! Retorna um DTO com os dados seguros do cliente.
            return converterParaDTO(clienteOptional.get());
        }

        // Se chegou até aqui, o login falhou.
        throw new IllegalArgumentException("E-mail ou senha inválidos.");
    }

    /**
     * Busca uma entidade Cliente pelo seu ID.
     * @param id O ID do cliente a ser buscado.
     * @return A entidade Cliente.
     * @throws ClienteNaoEncontrado se o cliente não for encontrado.
     */
    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado(id));
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
}