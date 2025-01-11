package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Cidade;
import com.example.especialista.spring.rest.domain.model.Estado;
import com.example.especialista.spring.rest.domain.model.input.CidadeInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDisAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInput cidadeInput){
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade){
        // Para evitar org.hibernate.HibernateException: identifier of an instance of
        cidade.setEstado(new Estado());
       modelMapper.map(cidadeInput, cidade);
    }

}
