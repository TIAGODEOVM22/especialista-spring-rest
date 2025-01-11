package com.example.especialista.spring.rest.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeModel {

    private Long id;
    private String nome;
    private EstadoModel estado;
}
