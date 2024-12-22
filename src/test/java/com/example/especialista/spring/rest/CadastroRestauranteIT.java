package com.example.especialista.spring.rest;

import com.example.especialista.spring.rest.domain.model.Cozinha;
import com.example.especialista.spring.rest.domain.model.Restaurante;
import com.example.especialista.spring.rest.domain.repository.CozinhaRepository;
import com.example.especialista.spring.rest.domain.repository.RestauranteRepository;
import com.example.especialista.spring.rest.util.DatabaseCleaner;
import com.example.especialista.spring.rest.util.ResourceUtils;
import com.example.especialista.spring.rest.util.TestMessages;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

    private String jsonCorretoNovoRestaurante;
    private String jsonRestauranteSemTaxaFrete;
    private String jsonRestauranteSemCozinha;
    private String jsonRestauranteComCozinhaInexistente;
    private static final Logger logger = LoggerFactory.getLogger(CadastroRestauranteIT.class);

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    private Restaurante novoRestauranteCorumba;
    private int quantidadeDeRestaurantesCadastrados;

    @Before
    public void setup(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = "/restaurantes";
        RestAssured.port = port;

        jsonCorretoNovoRestaurante = ResourceUtils.getContentFromResource(
                "/json/correto/novo-restaurante.json");
        jsonRestauranteSemTaxaFrete = ResourceUtils.getContentFromResource(
                "/json/incorreto/novo-restaurante-sem-taxaFrete.json");
        jsonRestauranteSemCozinha = ResourceUtils.getContentFromResource(
                "/json/incorreto/novo-restaurante-sem-cozinha.json");
        jsonRestauranteComCozinhaInexistente = ResourceUtils.getContentFromResource(
                "/json/incorreto/novo-restaurante-com-cozinha-inexistente.json");

        databaseCleaner.clearTables();
        prepararDados();
    }

    private void prepararDados() {
        Cozinha cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);

        novoRestauranteCorumba = new Restaurante();
        novoRestauranteCorumba.setNome("Novo Restaurante Corumba");
        novoRestauranteCorumba.setTaxaFrete(new BigDecimal(12));
        novoRestauranteCorumba.setCozinha(cozinhaAmericana);
        restauranteRepository.save(novoRestauranteCorumba);

       quantidadeDeRestaurantesCadastrados = (int) restauranteRepository.count();
    }

    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestaurantes() {


        given()
                .pathParam("restauranteId", novoRestauranteCorumba.getId()) // Configura o parâmetro do caminho
                .accept(ContentType.JSON) // Define o tipo de resposta esperada como JSON
                .when()
                .get("/{restauranteId}") // Faz a requisição GET usando o ID fornecido
                .then()
                .statusCode(HttpStatus.OK.value()) // Valida que o status retornado é 200 OK
                .body("nome", equalTo(novoRestauranteCorumba.getNome())); // Verifica que o nome no corpo da resposta é igual ao esperado
    }


    @Test
    public void deveRetornarQuantidadeCorretaDeRestaurantes() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("", hasSize(quantidadeDeRestaurantesCadastrados));
    }

    @Test
    public void deveRetornarStatus201_QuandoCadastrarRestaurante(){
        given()
                .body(jsonCorretoNovoRestaurante)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deveRetornarStatus200_QuandoConsultarRestaurante(){
        given()
                .contentType(ContentType.JSON)
                .when().get()
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete(){
        given()
                .body(jsonRestauranteSemTaxaFrete)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .log().ifValidationFails();
        logger.info("Teste concluído: {}", TestMessages.MENSAGEM_ERRO_RESTAURANTE_SEM_TAXA_FRETE);
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha(){
        given()
                .body(jsonRestauranteSemCozinha)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .log().ifValidationFails();
        logger.info("Teste concluído: {}", TestMessages.MENSAGEM_ERRO_RESTAURANTE_SEM_COZINHA);

    }

    @Test
    public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente(){
        given()
                .pathParam("restauranteId", TestMessages.ID_RESTAURANTE_INEXISTENTE)
                .contentType(ContentType.JSON)
                .when().get("/{restauranteId}")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente(){
        given()
                .body(jsonRestauranteComCozinhaInexistente)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when().post()
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .log().ifValidationFails();
        logger.info("Teste concluído: {}", TestMessages.RESTAURANTE_COM_COZINHA_INEXISTENTE);
    }
}
