package com.example.especialista.spring.rest.api.controller;

import com.example.especialista.spring.rest.assembler.CidadeInputDisAssembler;
import com.example.especialista.spring.rest.assembler.CidadeModelAssembler;
import com.example.especialista.spring.rest.domain.exception.EstadoNaoEncontradoException;
import com.example.especialista.spring.rest.domain.exception.NegocioException;
import com.example.especialista.spring.rest.domain.model.Cidade;
import com.example.especialista.spring.rest.domain.model.dto.CidadeModel;
import com.example.especialista.spring.rest.domain.model.input.CidadeInput;
import com.example.especialista.spring.rest.domain.repository.CidadeRepository;
import com.example.especialista.spring.rest.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;

    @Autowired
    private CidadeInputDisAssembler cidadeInputDisAssembler;

    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {

        return cidadeModelAssembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));
    }

    @GetMapping
    public List<CidadeModel> listar() {

        return cidadeModelAssembler.toColletionModel( cidadeRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
           Cidade cidade = cidadeInputDisAssembler.toDomainObject(cidadeInput);
           cidade = cadastroCidade.salvar(cidade);
           return cidadeModelAssembler.toModel(cidade);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{cidadeId}")
    public CidadeModel atualizar(@PathVariable Long cidadeId,
                            @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

           cidadeInputDisAssembler.copyToDomainObject(cidadeInput,cidadeAtual);

           cidadeAtual = cadastroCidade.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidade.excluir(cidadeId);
    }

}