package com.example.especialista.spring.rest.api.controller;

import com.example.especialista.spring.rest.assembler.EstadoInputDisAssembler;
import com.example.especialista.spring.rest.assembler.EstadoModelAssembler;
import com.example.especialista.spring.rest.domain.model.Estado;
import com.example.especialista.spring.rest.domain.model.dto.EstadoModel;
import com.example.especialista.spring.rest.domain.model.input.EstadoInput;
import com.example.especialista.spring.rest.domain.repository.EstadoRepository;
import com.example.especialista.spring.rest.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;*/

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstado;

    @Autowired
    private EstadoInputDisAssembler estadoInputDisAssembler;

    @Autowired
    private EstadoModelAssembler estadoModelAssembler;

    @GetMapping
    public List<EstadoModel> listar() {

        List<Estado> todosEstados = estadoRepository.findAll();

       return estadoModelAssembler.toColletionModel(todosEstados);
    }

    @GetMapping("/{estadoId}")
    public EstadoModel buscar(@PathVariable Long estadoId) {

        Estado estado = cadastroEstado.buscarOuFalhar(estadoId);

        return estadoModelAssembler.toModel(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisAssembler.toDomainObject(estadoInput);

        estado = cadastroEstado.salvar(estado);

        return estadoModelAssembler.toModel(estado);
    }

    @PutMapping("/{estadoId}")
    public EstadoModel atualizar(@PathVariable Long estadoId,
                            @RequestBody @Valid EstadoInput estadoInput) {
        Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);

        estadoInputDisAssembler.copyToDomainObject(estadoInput, estadoAtual);

        return estadoModelAssembler.toModel(cadastroEstado.salvar(estadoAtual));
    }

//    @PutMapping("/{estadoId}")
//    public Estado atualizar(@PathVariable Long estadoId,
//                            @RequestBody @Valid Estado estado) {
//        Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);
//
//        BeanUtils.copyProperties(estado, estadoAtual, "id");
//
//        return cadastroEstado.salvar(estadoAtual);
//    }

    @DeleteMapping("/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long estadoId) {

        cadastroEstado.excluir(estadoId);
    }

}