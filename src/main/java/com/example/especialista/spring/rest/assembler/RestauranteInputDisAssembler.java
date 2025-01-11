package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.model.Restaurante;
import com.example.especialista.spring.rest.domain.model.input.RestauranteInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestauranteInputDisAssembler {

    @Autowired
    private ModelMapper modelMapper;

    /*TRANSFORMA DE RESTAURANTE.INPUT dto PARA RESTAURANTE. domain*/
    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }

    /*Ajuda a mapear de forma inteligente sem ter que expecificar as propriedades*/
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
        /*para evitar HibernetException*/
        restaurante.setCozinha(new Cozinha());
        modelMapper.map(restauranteInput, restaurante);
    }

}

//        Cozinha cozinha = new Cozinha();
//        cozinha.setId(restauranteInput.getCozinha().getId());
//
//        Restaurante restaurante = new Restaurante();
//        restaurante.setNome(restauranteInput.getNome());
//        restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());
//        restaurante.setCozinha(cozinha);
//        return restaurante;


