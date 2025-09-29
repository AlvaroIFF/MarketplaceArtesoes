package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LojaNaoEncontradaException extends RuntimeException {

    public LojaNaoEncontradaException(String message) {
        super(message);
    }

    public LojaNaoEncontradaException(Long id) {
        super("Loja não encontrada: " + id);
    }

}
