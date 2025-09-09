package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontrado extends RuntimeException {

    public ProdutoNaoEncontrado(String message) {
        super(message);
    }

    public ProdutoNaoEncontrado(Long id) {
        super("Produto com o ID '" + id + "' não encontrado.");
    }
}