package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.LojaDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.LojaNaoEncontradaException;
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
}