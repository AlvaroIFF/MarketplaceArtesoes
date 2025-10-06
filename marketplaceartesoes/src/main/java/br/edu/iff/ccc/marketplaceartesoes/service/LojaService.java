package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.LojaApiUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.LojaDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.LojaDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.LojaNaoEncontradaException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.repository.LojaRepository;
import br.edu.iff.ccc.marketplaceartesoes.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LojaService {

    private final LojaRepository lojaRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public LojaService(LojaRepository lojaRepository, ProdutoRepository produtoRepository) {
        this.lojaRepository = lojaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public LojaDTO buscarLojaPorId(Long id) {
        Loja loja = lojaRepository.findById(id)
                .orElseThrow(() -> new LojaNaoEncontradaException("Loja de ID: " + id + " não encontrada"));

        List<ProdutoDTO> produtos = produtoRepository.findByLojaId(id).stream()
                .map(produto -> new ProdutoDTO(
                        produto.getId(),
                        produto.getNome(),
                        produto.getImagemPrincipalUrl(),
                        produto.getPreco(),
                        produto.getLoja().getArtesao().getNome(),
                        produto.getEstoque()
                ))
                .collect(Collectors.toList());

        return new LojaDTO(loja, produtos);
    }

    /**
    * Busca todas as lojas e as converte para o DTO detalhado da API.
     */
    @Transactional(readOnly = true)
    public List<LojaDetalheDTO> buscarTodas() {
        return lojaRepository.findAll().stream()
                .map(LojaDetalheDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca uma loja específica por ID e a converte para o DTO detalhado da API.
     */
    @Transactional(readOnly = true)
    public LojaDetalheDTO buscarPorId(Long id) {
        return lojaRepository.findById(id)
                .map(LojaDetalheDTO::new)
            .orElseThrow(() -> new RegraDeNegocioException("Loja não encontrada com o ID: " + id)); // Use uma exceção customizada
    }

    @Transactional
    public LojaDetalheDTO atualizarLoja(Long lojaId, LojaApiUpdateDTO dto, Long artesaoId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new RegraDeNegocioException("Loja não encontrada com o ID: " + lojaId));

        if (!loja.getArtesao().getId().equals(artesaoId)) {
            throw new RegraDeNegocioException("Você não tem permissão para editar esta loja.");
        }

        loja.setNome(dto.nome());
        loja.setDescricao(dto.descricao());
        loja.setCnpj(dto.cnpj());
        loja.setImagemBannerUrl(dto.imagemBannerUrl());

        Loja lojaAtualizada = lojaRepository.save(loja);
        return new LojaDetalheDTO(lojaAtualizada);
    }

    @Transactional
    public void deletarLoja(Long lojaId, Long artesaoId) {
        Loja loja = lojaRepository.findById(lojaId)
            .orElseThrow(() -> new RegraDeNegocioException("Loja não encontrada com o ID: " + lojaId));
    
        if (!loja.getArtesao().getId().equals(artesaoId)) {
            throw new RegraDeNegocioException("Você não tem permissão para deletar esta loja.");
        }

        lojaRepository.delete(loja);
    }
}