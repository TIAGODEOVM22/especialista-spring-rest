package com.example.especialista.spring.rest.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException{

    public CidadeNaoEncontradaException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;


    public CidadeNaoEncontradaException(Long cidadeId) {
        this(String.format("Não existe um cadastro de cidade com código %d", cidadeId));
    }


}
