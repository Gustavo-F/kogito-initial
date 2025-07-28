package br.com.gustavofs.credito;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;

import br.com.gustavofs.JsonLoader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class AvaliaCreditoDMNTest {
    
    @Value("${kogito.service.url}")
    private String baseURI;

    @Autowired
    private ResourceLoader resourceLoader;

    private RequestSpecification specification;

    @BeforeAll
    public void setup() {
        specification = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(baseURI)
            .build();
    }

    @Test
    public void testQuandoClienteMenorDeIdade() throws IOException {
        String jsonBody = JsonLoader.getPayloadFileContent(
            resourceLoader, "dmn_quandoClienteMenorDeIdade.json");

        RestAssured.given()
                .spec(specification)
                .body(jsonBody)
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(false))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Cliente deve ser maior de idade para a obtenção de crédito."));
    }

    @Test
    public void testQuandoSaldoMensalNegativo() throws IOException {
        String jsonBody = JsonLoader.getPayloadFileContent(
            resourceLoader, "dmn_quandoSaldoMensalNegativo.json");

        RestAssured.given()
                .spec(specification)
                .body(jsonBody)
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Cliente possui saldo mensal zerado ou negativo."));
    }

    @Test
    public void testQuandoSaldoMensalIncompativelComValorSolicitado() throws IOException {
        String jsonBody = JsonLoader.getPayloadFileContent(
            resourceLoader, "dmn_quandoSaldoMensalIncompativel.json");

        RestAssured.given()
                .spec(specification)
                .body(jsonBody)
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Saldo mensal do cliente é incompatível com o valor solicitado."));
    }

    @Test
    public void testQuandoCreditoAprovado() throws IOException {
        String jsonBody = JsonLoader.getPayloadFileContent(
            resourceLoader, "dmn_quandoCreditoAprovado.json");

        RestAssured.given()
                .spec(specification)
                .body(jsonBody)
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(true))
                .body("ProcessaSolicitacao.motivo", equalTo("O cliente cumpre com os requisitos de idade e saldo mensal necessários."));
    }
}
