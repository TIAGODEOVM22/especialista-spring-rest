package com.example.especialista.spring.rest.assembler;

import com.example.especialista.spring.rest.domain.model.Restaurante;
import com.example.especialista.spring.rest.domain.model.dto.CozinhaModel;
import com.example.especialista.spring.rest.domain.model.dto.RestauranteModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteModelAssembler {

    @Autowired
    private ModelMapper modelMapper;

    /*TRANSFORMA DE DOMAIN PARA MODEL dto*/
    public RestauranteModel toModel(Restaurante restaurante) {
        return modelMapper.map(restaurante, RestauranteModel.class);
    }
//        CozinhaModel cozinhaModel = new CozinhaModel();
//        cozinhaModel.setId(restaurante.getCozinha().getId());
//        cozinhaModel.setNome(restaurante.getCozinha().getNome());
//
//        RestauranteModel restauranteModel = new RestauranteModel();
//        restauranteModel.setId(restaurante.getId());
//        restauranteModel.setNome(restaurante.getNome());
//        restauranteModel.setTaxaFrete(restaurante.getTaxaFrete());
//        restauranteModel.setCozinha(cozinhaModel);
//        return restauranteModel;


    /*TRANSFORMA LISTA DE REST domain PARA RESTAURANTEMODEL dto*/
    public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
        return restaurantes.stream()
                .map(restaurante -> toModel(restaurante))
                .collect(Collectors.toList());
    }
}
