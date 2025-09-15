package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontrado extends RuntimeException {

    public ClienteNaoEncontrado(String message) {
        super(message);
    }

    public ClienteNaoEncontrado(Long id) {
        super("Cliente com o ID '" + id + "' não encontrado.");
    }
}