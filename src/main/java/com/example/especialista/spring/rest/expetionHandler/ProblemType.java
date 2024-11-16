package com.example.especialista.spring.rest.expetionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    DADOS_INVALIDOS("/dados-invalidos", "Dados Inválidos"),
    ERRO_DE_SISTEMA("erro-de-sistema", "Erro de Sistema"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem inconpreensível"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String uri;
    private String title;

    ProblemType(String path, String title) {
        this.uri = "https://especialista_spring_rest.com.br" + path;
        this.title = title;
    }

    public String getUri() {
        return uri;
    }
    public String getTitle() {
        return title;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
