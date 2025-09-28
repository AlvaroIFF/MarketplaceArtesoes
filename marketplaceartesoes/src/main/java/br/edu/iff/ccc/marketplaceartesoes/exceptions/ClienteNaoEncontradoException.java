package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(String message) {
        super(message);
    }

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente com o ID '" + id + "' não encontrado.");
    }
}