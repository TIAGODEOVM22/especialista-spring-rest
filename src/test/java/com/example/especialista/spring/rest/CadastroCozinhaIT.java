package com.example.especialista.spring.rest;


import com.example.especialista.spring.rest.domain.exception.CozinhaNaoEncontradaException;
import com.example.especialista.spring.rest.domain.exception.EntidadeEmUsoException;
import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.service.CadastroCozinhaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CadastroCozinhaIT {

    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Test
    public void testarCadastroCozinhaComSucesso(){
        //cenario
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        //ação
        novaCozinha = cadastroCozinhaService.salvar(novaCozinha);

        //validação
        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();

    }

    @Test(expected = ConstraintViolationException.class)/*se a excepetion for lançada o teste passa*/
    public  void testarCadastroCozinhaSemNome(){
        Cozinha cozinha = new Cozinha();
        cozinha.setNome(null);
        cozinha = cadastroCozinhaService.salvar(cozinha);
    }

    @Test(expected = EntidadeEmUsoException.class)
    public void deveFalha_QuandoExcluirCozinhaEmUso(){
        cadastroCozinhaService.excluir(1L);
    }

    @Test(expected = CozinhaNaoEncontradaException.class)
    public void deveFalha_QuandoExcluirCozinhaInexistente(){
        cadastroCozinhaService.excluir(100L);

    }

}
