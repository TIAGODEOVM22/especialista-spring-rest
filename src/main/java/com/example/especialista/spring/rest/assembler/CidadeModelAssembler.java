package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Cidade;
import com.example.especialista.spring.rest.domain.model.dto.CidadeModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CidadeModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CidadeModel toModel (Cidade cidade){
        return modelMapper.map(cidade, CidadeModel.class);
    }

    public List<CidadeModel> toColletionModel (List<Cidade> cidades){
        return cidades.stream()
                .map(cidade -> toModel(cidade))
                .collect(Collectors.toList());
    }

}
