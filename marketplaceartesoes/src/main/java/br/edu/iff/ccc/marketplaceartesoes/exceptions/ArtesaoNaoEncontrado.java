package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ArtesaoNaoEncontrado extends RuntimeException {

    public ArtesaoNaoEncontrado(String message) {
        super(message);
    }

    public ArtesaoNaoEncontrado(Long id) {
        super("Artesão com o ID '" + id + "' não encontrado.");
    }
}