package com.example.especialista.spring.rest.api.controller;

import com.example.especialista.spring.rest.assembler.RestauranteInputDisAssembler;
import com.example.especialista.spring.rest.assembler.RestauranteModelAssembler;
import com.example.especialista.spring.rest.domain.exception.CozinhaNaoEncontradaException;
import com.example.especialista.spring.rest.domain.exception.NegocioException;
import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.model.Restaurante;
import com.example.especialista.spring.rest.domain.model.dto.RestauranteModel;
import com.example.especialista.spring.rest.domain.model.input.RestauranteInput;
import com.example.especialista.spring.rest.domain.repository.RestauranteRepository;
import com.example.especialista.spring.rest.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteInputDisAssembler restauranteInputDisAssembler;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @GetMapping
    public List<RestauranteModel> listar() {
        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    @GetMapping("/{restauranteId}")
    public RestauranteModel buscar(@PathVariable Long restauranteId) {
       Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

       return restauranteModelAssembler.toModel(restaurante);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restaurante = restauranteInputDisAssembler.toDomainObject(restauranteInput); //transforma de dto para domain

            return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante)) ; //transforma de domain para dto e salva
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public RestauranteModel atualizar(@PathVariable Long restauranteId,
                                 @RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
            restauranteInputDisAssembler.copyToDomainObject(restauranteInput, restauranteAtual);

//            BeanUtils.copyProperties(restaurante, restauranteAtual,
//                    "id", "formasPagamento", "endereco", "dataCadastro", "produtos");

            return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

}