package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ArtesaoNaoEncontrado; 
import br.edu.iff.ccc.marketplaceartesoes.repository.ArtesaoRepository; 
import br.edu.iff.ccc.marketplaceartesoes.repository.LojaRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ArtesaoService {

    private final ArtesaoRepository artesaoRepository;
    private final LojaRepository lojaRepository;

    @Autowired
    public ArtesaoService(ArtesaoRepository artesaoRepository, LojaRepository lojaRepository) {
        this.artesaoRepository = artesaoRepository;
        this.lojaRepository = lojaRepository;
    }


    /**
     * Cria um novo Artesão e sua Loja, salvando-os no banco de dados.
     */
    @Transactional // Garante que a operação seja atômica
    public void cadastrarArtesao(ArtesaoCadastroDTO dados) {
        if (!dados.senha().equals(dados.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem!");
        }
        if (artesaoRepository.findByEmail(dados.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso por outro artesão.");
        }
        if (lojaRepository.findByCnpj(dados.cnpj()).isPresent()) {
            throw new IllegalArgumentException("Este CNPJ já está em uso por outra loja.");
        }
        if (lojaRepository.findByNome(dados.nomeLoja()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma loja com este nome.");
        }

        Artesao novoArtesao = new Artesao(
                dados.nome(),
                dados.cpf(),
                dados.dtNasc(),
                dados.numContato(),
                dados.email(),
                dados.senha(), 
                null
        );

        Loja novaLoja = new Loja(
                dados.nomeLoja(),
                dados.descricaoLoja(),
                dados.cnpj(),
                novoArtesao
        );

        novoArtesao.setLoja(novaLoja);

        artesaoRepository.save(novoArtesao);
        
        System.out.println("Artesão cadastrado com sucesso: " + novoArtesao.getNome() + " (ID: " + novoArtesao.getId() + ")");
        System.out.println("Loja criada: " + novaLoja.getNome());
    }

    /**
     * Tenta autenticar um artesão.
     * @param email O email para login.
     * @param senha A senha para login.
     * @return um ArtesaoDTO se o login for bem-sucedido.
     * @throws IllegalArgumentException se as credenciais forem inválidas.
     */
    @Transactional(readOnly = true)
    public ArtesaoDTO fazerLogin(String email, String senha) {
        Optional<Artesao> artesaoOptional = artesaoRepository.findByEmail(email);

        if (artesaoOptional.isPresent() && artesaoOptional.get().getSenha().equals(senha)) {
            return converterParaDTO(artesaoOptional.get());
        }
        throw new IllegalArgumentException("E-mail ou senha inválidos para artesão.");
    }

    /**
     * Busca uma entidade Artesão completa pelo seu ID.
     * @param id O ID do artesão a ser buscado.
     * @return A entidade Artesao.
     * @throws ArtesaoNaoEncontradoException se o artesão não for encontrado.
     */
    @Transactional(readOnly = true)
    public Artesao buscarEntidadePorId(Long id) {
        return artesaoRepository.findById(id)
                .orElseThrow(() -> new ArtesaoNaoEncontrado(id));
    }
    
    // Método auxiliar para buscar um artesão por e-mail (agora usa o repositório)
    @Transactional(readOnly = true)
    public Optional<Artesao> buscarPorEmail(String email) {
        return artesaoRepository.findByEmail(email);
    }

    private ArtesaoDTO converterParaDTO(Artesao artesao) {
        String nomeLoja = artesao.getLoja() != null ? artesao.getLoja().getNome() : null;
        String descricaoLoja = artesao.getLoja() != null ? artesao.getLoja().getDescricao() : null;

        return new ArtesaoDTO(
            artesao.getId(),
            artesao.getNome(),
            artesao.getEmail(),
            nomeLoja,
            descricaoLoja
        );
    }
}