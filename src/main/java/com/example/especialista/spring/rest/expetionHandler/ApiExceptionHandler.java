package com.example.especialista.spring.rest.expetionHandler;

import com.example.especialista.spring.rest.domain.exception.EntidadeEmUsoException;
import com.example.especialista.spring.rest.domain.exception.EntidadeNaoEncontradaException;
import com.example.especialista.spring.rest.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*CONTROLADOR DE EX PARA TODAS AS CLASSES*/
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    public static final String USER_MESSAGE_GENERICO = "Ocorreu um erro interno inesperado no sistema. "
            + "Tente novamente e se o problema persistir, entre em contato "
            + "com o administrador do sistema.";

    /*TRATA A EX DE CONSTRATINTS VIOLADAS
    * retornando uma lista com os atributos que possuem erros*/
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();

        List<Problem.Field> problemFields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> Problem.Field.builder()
                        .name(fieldError.getField())
                        .userMessage(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(problemFields)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /*TRATA QUANDO PASSAMOS UMA PROPRIEDADE DESCONHECIDA*/
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {

        // Criei o método joinPath para reaproveitar em todos os métodos que precisam
        // concatenar os nomes das propriedades (separando por ".")
        String path = joinPath(ex.getPath());/*caminho completo da propriedade que causou o erro*/

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /*TRATA QUANDO PASSAMOS ALGUMA INFORMAÇÃO ERRADA NO CORPO DO JSON*/
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex); /*Obtenção da causa raiz */

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /*TRATA QUANDO JSON RECEBE UM TIPO DE VALOR INCORRETO*/
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
                                                       HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /*TRATA QUANDO TENTAMOS DELETAR UM OBJTO QUE ESTA COM CHAVE ESTRANGEIRA EM USO*/
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }


    /*TRATA QUANDO NÃO ENCONTRADA A ENTIDADE, QUANDO PASSAMOS O ID INEXISTENTE*/
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontrada(
            EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    /*CAPTURA ERRO DE NEGOCIO NÃO ENCONTRADO E DEVOLVE STATUS 400*/
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

//    Se body for null: Um objeto do tipo Problem é criado
//    Se body for uma String: Ele constrói um objeto Problem semelhante ao acima,
//    mas com o title preenchido com o valor da String recebida no body
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(USER_MESSAGE_GENERICO)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(USER_MESSAGE_GENERICO)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    //TRATA DOIS POSSÍVEIS ERROS// SE a exceção for uma MethodArgumentTypeMismatchException,
    //o método chama noso método handleMethodArgumentTypeMismatch
    //SE a exceção não for do tipo MethodArgumentTypeMismatchException,
    //o método chama super.handleTypeMismatch, que delega o tratamento ao
    //comportamento padrão do Spring para TypeMismatchException
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    /*EX: O parâmetro de URL 'id' recebeu o valor 'abc', que é de um tipo inválido.*/
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /*_______CONSTROI UM PROBLEMA JÁ PREENCHIDO______*/
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status,
                                                        ProblemType problemType, String detail) {
        return Problem.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail);
    }

    /*AJUDA A SEPARAR NOMES DE ATRIBUTOS COM PROBLEMAS NA DESERIALIZAÇÃO ENTRE OUTROS*/
    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()/*Usa stream() para criar um fluxo dos elementos da lista references.*/
                .map(JsonMappingException.Reference::getFieldName)/*extrai o nome de cada campo Reference na lista.*/
                .collect(Collectors.joining("."));/*separa com PONTO os nomes da lista Reference*/

        /*EM JsonMappingExceptioN estão incluídas referências
         que representam o "caminho" do campo problemático*/
    }

    /*__________TRATA ERROS DE URL INEXISTENTE________*/
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);

    }

    /*_______TRATA ERROS GERAIS QUE NÃO FORAM CAPTURADOS_______*/
    @ExceptionHandler(Exception.class)
    /* essa anotação garante que este métodp é chamado quando Exception.class ou qualquer uma
    de suas subclasses for lançada durante o processamento de uma requisição.*/
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = USER_MESSAGE_GENERICO;

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(USER_MESSAGE_GENERICO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

}