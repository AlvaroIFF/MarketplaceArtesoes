package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoApiUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoUpdateDTO; 
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ArtesaoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.AutenticacaoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.repository.ArtesaoRepository;
import br.edu.iff.ccc.marketplaceartesoes.repository.LojaRepository;   // <-- NOVO IMPORT
import br.edu.iff.ccc.marketplaceartesoes.repository.ProdutoRepository; // <-- NOVO IMPORT
import br.edu.iff.ccc.marketplaceartesoes.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtesaoService {

    private final ArtesaoRepository artesaoRepository;
    private final LojaRepository lojaRepository;
    private final FileStorageService fileStorageService;
    private final ProdutoRepository produtoRepository; 
    private final PedidoRepository pedidoRepository;

    @Autowired
    public ArtesaoService(ArtesaoRepository artesaoRepository, LojaRepository lojaRepository, FileStorageService fileStorageService, ProdutoRepository produtoRepository, PedidoRepository pedidoRepository) {
        this.artesaoRepository = artesaoRepository;
        this.lojaRepository = lojaRepository;
        this.fileStorageService = fileStorageService;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public void cadastrarArtesao(ArtesaoCadastroDTO dados, MultipartFile fotoPerfil, MultipartFile fotoLoja) {
        if (!dados.senha().equals(dados.confirmarSenha())) {
            throw new RegraDeNegocioException("As senhas não conferem!");
        }
        if (artesaoRepository.findByEmail(dados.email()).isPresent()) {
            throw new RegraDeNegocioException("Este e-mail já está em uso por outro artesão.");
        }
        if (artesaoRepository.findByCpf(dados.cpf()).isPresent()) {
            throw new RegraDeNegocioException("Este CPF já está em uso por outro artesão.");
        }
        if (dados.cnpj() != null && !dados.cnpj().isBlank() && lojaRepository.findByCnpj(dados.cnpj()).isPresent()) {
            throw new RegraDeNegocioException("Este CNPJ já está em uso por outra loja.");
        }
        if (lojaRepository.findByNome(dados.nomeLoja()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma loja com este nome.");
        }

        String fotoPerfilUrl = fileStorageService.salvarImagem(fotoPerfil);
        String imagemBannerUrl = fileStorageService.salvarImagem(fotoLoja);

        Artesao novoArtesao = new Artesao(
                dados.nome(),
                dados.cpf(),
                dados.dtNasc(),
                dados.numContato(),
                dados.email(),
                dados.senha(),
                fotoPerfilUrl
        );

        Loja novaLoja = new Loja(
                dados.nomeLoja(),
                dados.descricaoLoja(),
                dados.cnpj(),
                novoArtesao
        );
        
        novaLoja.setImagemBannerUrl(imagemBannerUrl);
        novoArtesao.setLoja(novaLoja);
        artesaoRepository.save(novoArtesao);

        System.out.println("Artesão cadastrado com sucesso: " + novoArtesao.getNome() + " (ID: " + novoArtesao.getId() + ")");
        System.out.println("Loja criada: " + novaLoja.getNome());
    }

    /**
     * Atualiza os dados de um artesão e sua loja.
     * @param id O ID do artesão a ser atualizado.
     * @param dadosUpdate DTO com as novas informações.
     * @return DTO do artesão com os dados atualizados.
     */
    @Transactional
    public ArtesaoDTO atualizarArtesao(Long id, ArtesaoUpdateDTO dadosUpdate) {
        // 1. Busca o artesão e sua loja no banco de dados
        Artesao artesao = artesaoRepository.findById(id)
                .orElseThrow(() -> new ArtesaoNaoEncontradoException(id));
        Loja loja = artesao.getLoja();
        if (loja == null) {
            throw new RegraDeNegocioException("Inconsistência encontrada: Artesão não possui uma loja associada.");
        }

        // 2. Limpa os dados de entrada (remove formatação de CPF e CNPJ)
        String cpfLimpo = dadosUpdate.cpf() != null ? dadosUpdate.cpf().replaceAll("[^0-9]", "") : null;
        String cnpjLimpo = dadosUpdate.cnpj() != null ? dadosUpdate.cnpj().replaceAll("[^0-9]", "") : null;

        // 3. Realiza as validações usando os métodos específicos do repositório
        if (artesaoRepository.findByEmailAndIdNot(dadosUpdate.email(), artesao.getId()).isPresent()) {
            throw new RegraDeNegocioException("O e-mail informado já está em uso por outro artesão.");
        }
        if (artesaoRepository.findByCpfAndIdNot(cpfLimpo, artesao.getId()).isPresent()) {
            throw new RegraDeNegocioException("O CPF informado já está em uso por outro artesão.");
        }
        if (lojaRepository.findByNomeAndIdNot(dadosUpdate.nomeLoja(), loja.getId()).isPresent()) {
            throw new RegraDeNegocioException("Já existe uma loja com este nome.");
        }
        if (cnpjLimpo != null && !cnpjLimpo.isBlank() && lojaRepository.findByCnpjAndIdNot(cnpjLimpo, loja.getId()).isPresent()) {
            throw new RegraDeNegocioException("O CNPJ informado já está em uso por outra loja.");
        }

        // 4. Atualiza a entidade do artesão com os dados do DTO
        artesao.setNome(dadosUpdate.nome());
        artesao.setEmail(dadosUpdate.email());
        artesao.setCpf(cpfLimpo);
        artesao.setDtNasc(dadosUpdate.dtNasc());
        artesao.setNumContato(dadosUpdate.numContato());

        // 5. Atualiza a entidade da loja com os dados do DTO
        loja.setNome(dadosUpdate.nomeLoja());
        loja.setDescricao(dadosUpdate.descricaoLoja());
        loja.setCnpj(cnpjLimpo);

        // 6. Salva a entidade do artesão (a loja será atualizada em cascata)
        Artesao artesaoAtualizado = artesaoRepository.save(artesao);

        // 7. Retorna um DTO com os dados atualizados
        return converterParaDTO(artesaoAtualizado);
    }

    @Transactional
    public void deletarArtesaoE_Loja(Long artesaoId) {
        Artesao artesao = artesaoRepository.findById(artesaoId)
                .orElseThrow(() -> new ArtesaoNaoEncontradoException(artesaoId));

        if (!pedidoRepository.findPedidosByArtesaoId(artesaoId).isEmpty()) {
             throw new RegraDeNegocioException("Não é possível excluir o artesão, pois ele possui pedidos registrados como vendedor.");
        }

        Loja lojaDoArtesao = artesao.getLoja();
        if (lojaDoArtesao != null) {
            List<Produto> produtosDaLoja = produtoRepository.findByLojaId(lojaDoArtesao.getId());
            for (Produto produto : produtosDaLoja) {
                produtoRepository.delete(produto);
            }
        }
        
        if (lojaDoArtesao != null) {
            lojaRepository.delete(lojaDoArtesao);
        }

        artesaoRepository.delete(artesao);
    }

    @Transactional(readOnly = true)
    public ArtesaoDTO fazerLogin(String email, String senha) {
        Artesao artesao = artesaoRepository.findByEmail(email)
                .orElseThrow(() -> new AutenticacaoException("E-mail ou senha inválidos para artesão."));

        if (!artesao.getSenha().equals(senha)) {
            throw new AutenticacaoException("E-mail ou senha inválidos para artesão.");
        }
        
        return converterParaDTO(artesao);
    }

    @Transactional(readOnly = true)
    public Artesao buscarEntidadePorId(Long id) {
        return artesaoRepository.findById(id)
                .orElseThrow(() -> new ArtesaoNaoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public Optional<Artesao> buscarPorEmail(String email) {
        return artesaoRepository.findByEmail(email);
    }

    private ArtesaoDTO converterParaDTO(Artesao artesao) {
        return new ArtesaoDTO(artesao);
    }

    @Transactional
    public ArtesaoApiDTO cadastrarArtesaoApi(ArtesaoApiCadastroDTO dto) {
        // Validações de dados únicos
        if (artesaoRepository.existsByEmail(dto.email())) {
            throw new RegraDeNegocioException("O e-mail informado já está em uso.");
        }
        if (artesaoRepository.existsByCpf(dto.cpf())) {
            throw new RegraDeNegocioException("O CPF informado já está em uso.");
        }
        // Você também pode adicionar uma validação para o CNPJ e nome da loja se forem únicos

        // Cria a entidade Artesao
        Artesao novoArtesao = new Artesao(
            dto.nome(),
            dto.cpf(),
            dto.dtNasc(),
            dto.numContato(),
            dto.email(),
            dto.senha(), // Lembre-se da criptografia para a P2!
            null // fotoUrl inicia como nula na API
        );

        // Cria a entidade Loja e associa ao artesão
        Loja novaLoja = new Loja(
            dto.nomeLoja(),
            dto.descricaoLoja(),
            dto.cnpj(),
            novoArtesao
        );

        // Define a relação bidirecional
        novoArtesao.setLoja(novaLoja);

        // Salva o artesão (e a loja será salva em cascata)
        Artesao artesaoSalvo = artesaoRepository.save(novoArtesao);

        return new ArtesaoApiDTO(artesaoSalvo);
    }

    @Transactional(readOnly = true)
    public ArtesaoApiDTO buscarArtesaoPorId(Long id) {
        Artesao artesao = artesaoRepository.findById(id)
          .orElseThrow(() -> new RegraDeNegocioException("Artesão não encontrado com o ID: " + id));
        return new ArtesaoApiDTO(artesao);
    }

    @Transactional
    public ArtesaoApiDTO atualizarArtesao(Long id, ArtesaoApiUpdateDTO dto) {
        Artesao artesao = artesaoRepository.findById(id)
            .orElseThrow(() -> new RegraDeNegocioException("Artesão não encontrado com o ID: " + id));

        artesaoRepository.findByEmail(dto.email()).ifPresent(outroArtesao -> {
            if (!outroArtesao.getId().equals(id)) {
                throw new RegraDeNegocioException("O e-mail informado já está em uso por outro usuário.");
            }
        });

        // Atualiza dados do artesão
        artesao.setNome(dto.nome());
        artesao.setDtNasc(dto.dtNasc());
        artesao.setNumContato(dto.numContato());
        artesao.setEmail(dto.email());

        // Atualiza dados da loja
        Loja loja = artesao.getLoja();
        if (loja != null) {
            loja.setNome(dto.nomeLoja());
            loja.setDescricao(dto.descricaoLoja());
            loja.setCnpj(dto.cnpj());
        }

        Artesao artesaoAtualizado = artesaoRepository.save(artesao);
        return new ArtesaoApiDTO(artesaoAtualizado);
    }

    @Transactional
    public void deletarArtesao(Long id) {
        if (!artesaoRepository.existsById(id)) {
            throw new RegraDeNegocioException("Artesão não encontrado com o ID: " + id);
        }
        artesaoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ArtesaoApiDTO> buscarTodosArtesaos() {
        return artesaoRepository.findAll().stream()
                .map(ArtesaoApiDTO::new)
                .collect(Collectors.toList());
    }
}