package com.example.especialista.spring.rest.api.controller;

import com.example.especialista.spring.rest.assembler.CozinhaInputDisAssembler;
import com.example.especialista.spring.rest.assembler.CozinhaModelAssembler;
import com.example.especialista.spring.rest.domain.exception.CozinhaNaoEncontradaException;
import com.example.especialista.spring.rest.domain.exception.NegocioException;
import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.model.dto.CozinhaModel;
import com.example.especialista.spring.rest.domain.model.input.CozinhaInput;
import com.example.especialista.spring.rest.domain.repository.CozinhaRepository;
import com.example.especialista.spring.rest.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Autowired
    private CozinhaModelAssembler cozinhaModelAssembler;

    @Autowired
    private CozinhaInputDisAssembler cozinhaInputDisAssembler;

    @GetMapping
    public List<CozinhaModel> listar() {

        return cozinhaModelAssembler.toCollectionModel(cozinhaRepository.findAll());
    }

    @GetMapping("/{cozinhaId}")
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {

        return cozinhaModelAssembler.toModel(cadastroCozinha.buscarOuFalhar(cozinhaId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        try {
            Cozinha cozinha = cozinhaInputDisAssembler.toDomainObject(cozinhaInput);
            return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));
        } catch (CozinhaNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }


        @PutMapping("/{cozinhaId}")
        public CozinhaModel atualizar (@PathVariable Long cozinhaId , @RequestBody @Valid CozinhaInput cozinhaInput){

            Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
            cozinhaInputDisAssembler.copyToDomainObject(cozinhaInput, cozinha);

            return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));

        }

        @DeleteMapping("/{cozinhaId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void remover (@PathVariable Long cozinhaId){

            cadastroCozinha.excluir(cozinhaId);
        }


}

