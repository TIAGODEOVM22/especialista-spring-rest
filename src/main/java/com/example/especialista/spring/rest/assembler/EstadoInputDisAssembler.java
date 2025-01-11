package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Estado;
import com.example.especialista.spring.rest.domain.model.input.EstadoInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstadoInputDisAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public Estado toDomainObject(EstadoInput estadoInput) {
        return modelMapper.map(estadoInput, Estado.class);
    }

    /*Ajuda a mapear de forma inteligente sem ter que expecificar as propriedades*/
    public void copyToDomainObject(EstadoInput estadoInput, Estado estado) {
       
        modelMapper.map(estadoInput, estado);
    }


}
