package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ArtesaoService {

    // Lista em memória para simular o banco de dados de artesãos
    private static final List<Artesao> artesoesEmMemoria = new ArrayList<>();
    private static final AtomicLong idContador = new AtomicLong(100); // Começando de 100 para não colidir com IDs de clientes

    /**
     * Cria um novo Artesão e sua Loja, salvando-os na lista em memória.
     */
    public void cadastrarArtesao(ArtesaoCadastroDTO dados) {
        // Validações básicas
        if (!dados.senha().equals(dados.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem!");
        }
        
        // 1. Criar a entidade Artesao
        Artesao novoArtesao = new Artesao(
                dados.nome(),
                dados.cpf(),
                dados.dtNasc(),
                dados.numContato(),
                dados.email(),
                dados.senha(),
                null // fotoUrl inicial
        );
        novoArtesao.setId(idContador.getAndIncrement());

        // Criar a entidade Loja
        Loja novaLoja = new Loja(
                dados.nomeLoja(),
                dados.descricaoLoja(),
                dados.cnpj(),
                novoArtesao // <- Associando o artesão à loja no construtor
        );

        // Estabelecer a relação bidirecional
        novoArtesao.setLoja(novaLoja);
        
        // Salvar na lista em memória
        artesoesEmMemoria.add(novoArtesao);
        
        System.out.println("Artesão cadastrado com sucesso: " + novoArtesao.getNome());
        System.out.println("Loja criada: " + novaLoja.getNome());
    }

    /**
     * Tenta autenticar um artesão.
     * @param email O email para login.
     * @param senha A senha para login.
     * @return um ArtesaoDTO se o login for bem-sucedido, ou null se falhar.
     */
    public ArtesaoDTO fazerLogin(String email, String senha) {
        Artesao artesao = buscarPorEmail(email);
        if (artesao != null && artesao.getSenha().equals(senha)) {
            return converterParaDTO(artesao);
        }
        return null;
    }

    public Artesao buscarEntidadePorId(Long id) {
        return artesoesEmMemoria.stream()
                .filter(artesao -> artesao.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    private Artesao buscarPorEmail(String email) {
        return artesoesEmMemoria.stream()
                .filter(artesao -> artesao.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    private ArtesaoDTO converterParaDTO(Artesao artesao) {
        return new ArtesaoDTO(
            artesao.getId(),
            artesao.getNome(),
            artesao.getEmail(),
            artesao.getLoja().getNome(),
            artesao.getLoja().getDescricao()
        );
    }
}