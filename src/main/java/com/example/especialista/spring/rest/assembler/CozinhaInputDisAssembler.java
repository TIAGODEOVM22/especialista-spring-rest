package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.model.input.CozinhaInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CozinhaInputDisAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cozinha toDomainObject(CozinhaInput cozinhaInput) {

        return modelMapper.map(cozinhaInput, Cozinha.class);
    }

    /*Ajuda a mapear de forma inteligente sem ter que expecificar as propriedades*/
    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {

        modelMapper.map(cozinhaInput, cozinha);
    }
}
