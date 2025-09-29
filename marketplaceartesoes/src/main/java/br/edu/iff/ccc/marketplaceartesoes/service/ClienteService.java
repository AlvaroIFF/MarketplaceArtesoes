package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteUpdateDTO; // <-- Import the new DTO
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.AutenticacaoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ClienteNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.repository.ClienteRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import br.edu.iff.ccc.marketplaceartesoes.repository.PedidoRepository;

@Service
public class ClienteService {

  private final ClienteRepository clienteRepository;
  private final FileStorageService fileStorageService;
  private final PedidoRepository pedidoRepository;

  @Autowired
  public ClienteService(
    ClienteRepository clienteRepository,
    FileStorageService fileStorageService,
    PedidoRepository pedidoRepository
  ) {
    this.clienteRepository = clienteRepository;
    this.fileStorageService = fileStorageService;
    this.pedidoRepository = pedidoRepository;
  }

  @Transactional
  public void cadastrarCliente(
    ClienteCadastroDTO dadosCadastro,
    MultipartFile foto
  ) {
    if (!dadosCadastro.senha().equals(dadosCadastro.confirmarSenha())) {
      throw new RegraDeNegocioException("As senhas não conferem!");
    }

    if (clienteRepository.findByEmail(dadosCadastro.email()).isPresent()) {
      throw new RegraDeNegocioException("Este e-mail já está em uso.");
    }

    if (
      dadosCadastro.cpf() != null &&
      !dadosCadastro.cpf().isBlank() &&
      clienteRepository.findByCpf(dadosCadastro.cpf()).isPresent()
    ) {
      throw new RegraDeNegocioException("Este CPF já está em uso.");
    }

    String fotoUrl = fileStorageService.salvarImagem(foto);

    Cliente novoCliente = new Cliente(
      dadosCadastro.nome(),
      dadosCadastro.cpf(),
      dadosCadastro.dtNasc(),
      dadosCadastro.numContato(),
      dadosCadastro.email(),
      dadosCadastro.senha(),
      fotoUrl
    );

    clienteRepository.save(novoCliente);
    System.out.println(
      "Cliente cadastrado com sucesso: " +
      novoCliente.getNome() +
      " (ID: " +
      novoCliente.getId() +
      ")"
    );
  }

  /**
   * Atualiza os dados de um cliente existente.
   * @param id O ID do cliente a ser atualizado.
   * @param dadosUpdate DTO com as novas informações.
   */
  @Transactional
  public ClienteDTO atualizarCliente(Long id, ClienteUpdateDTO dadosUpdate) {
    // 1. Busca o cliente no banco de dados
    Cliente cliente = clienteRepository
      .findById(id)
      .orElseThrow(() -> new ClienteNaoEncontradoException(id));

    // 2. Valida se o novo E-MAIL já não está em uso por OUTRO cliente
    Optional<Cliente> clienteComNovoEmail = clienteRepository.findByEmail(
      dadosUpdate.email()
    );
    if (
      clienteComNovoEmail.isPresent() &&
      !clienteComNovoEmail.get().getId().equals(cliente.getId())
    ) {
      throw new RegraDeNegocioException(
        "O e-mail informado já está em uso por outro cliente."
      );
    }

    // 3. Valida se o novo CPF já não está em uso por OUTRO cliente
    Optional<Cliente> clienteComNovoCpf = clienteRepository.findByCpf(
      dadosUpdate.cpf()
    );
    if (
      clienteComNovoCpf.isPresent() &&
      !clienteComNovoCpf.get().getId().equals(cliente.getId())
    ) {
      throw new RegraDeNegocioException(
        "O CPF informado já está em uso por outro cliente."
      );
    }

    // 4. Atualiza os dados da entidade com os dados do DTO
    cliente.setNome(dadosUpdate.nome());
    cliente.setEmail(dadosUpdate.email());
    cliente.setCpf(dadosUpdate.cpf());
    cliente.setDtNasc(dadosUpdate.dtNasc());
    cliente.setNumContato(dadosUpdate.numContato());

    // 5. Salva a entidade atualizada
    Cliente clienteAtualizado = clienteRepository.save(cliente);

    // 6. Retorna um DTO com os dados atualizados
    return converterParaDTO(clienteAtualizado);
  }

  @Transactional
    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        if (!pedidoRepository.findByClienteId(id).isEmpty()) {
            throw new RegraDeNegocioException("Não é possível excluir o cliente, pois ele possui pedidos registrados.");
        }
        
        clienteRepository.delete(cliente);
    }

  @Transactional(readOnly = true)
  public ClienteDTO fazerLogin(String email, String senha) {
    Cliente cliente = clienteRepository
      .findByEmail(email)
      .orElseThrow(() -> new AutenticacaoException("E-mail ou senha inválidos."));

    if (!cliente.getSenha().equals(senha)) {
      throw new AutenticacaoException("E-mail ou senha inválidos.");
    }

    return converterParaDTO(cliente);
  }

  @Transactional(readOnly = true)
  public Cliente buscarEntidadePorId(Long id) {
    return clienteRepository
      .findById(id)
      .orElseThrow(() -> new ClienteNaoEncontradoException(id));
  }

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