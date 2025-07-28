package br.com.gustavofs.credito;

import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

import br.com.gustavofs.credito.models.Cliente;
import br.com.gustavofs.credito.models.Solicitacao;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class AvaliaCreditoDMNTest {
    
    @Value("${kogito.service.url}")
    private String baseURI;

    private RequestSpecification specification;

    @BeforeAll
    public void setup() {
        specification = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setBaseUri(baseURI)
            .build();
    }

    @Test
    public void testQuandoClienteMenorDeIdade() {
        Solicitacao solicitacao = Solicitacao.builder()
            .cliente(new Cliente("John Doe", LocalDate.now().minusYears(15).toString()))
            .rendaMensal(new BigDecimal(5000.00))
            .gastoMensal(new BigDecimal(3500.00))
            .valorSolicitado(new BigDecimal(15000.00))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("Solicitacao", solicitacao))
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(false))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Cliente deve ser maior de idade para a obtenção de crédito."));
    }

    @Test
    public void testQuandoSaldoMensalNegativo() {
        Solicitacao solicitacao = Solicitacao.builder()
            .cliente(new Cliente("John Doe", LocalDate.now().minusYears(50).toString()))
            .rendaMensal(new BigDecimal(5000.00))
            .gastoMensal(new BigDecimal(6500.00))
            .valorSolicitado(new BigDecimal(15000.00))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("Solicitacao", solicitacao))
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Cliente possui saldo mensal zerado ou negativo."));
    }

    @Test
    public void testQuandoSaldoMensalIncompativelComValorSolicitado() {
        Solicitacao solicitacao = Solicitacao.builder()
            .cliente(new Cliente("John Doe", LocalDate.now().minusYears(50).toString()))
            .rendaMensal(new BigDecimal(5000.00))
            .gastoMensal(new BigDecimal(4200.00))
            .valorSolicitado(new BigDecimal(50000.00))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("Solicitacao", solicitacao))
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(false))
                .body("ProcessaSolicitacao.motivo", equalTo("Saldo mensal do cliente é incompatível com o valor solicitado."));
    }

    @Test
    public void testQuandoCreditoAprovado() {
        Solicitacao solicitacao = Solicitacao.builder()
            .cliente(new Cliente("John Doe", LocalDate.now().minusYears(50).toString()))
            .rendaMensal(new BigDecimal(5000.00))
            .gastoMensal(new BigDecimal(3500.00))
            .valorSolicitado(new BigDecimal(5000.00))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("Solicitacao", solicitacao))
            .when()
                .post("/avalia-credito")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("MaiorIdade", equalTo(true))
                .body("ProcessaSolicitacao.aprovado", equalTo(true))
                .body("ProcessaSolicitacao.motivo", equalTo("O cliente cumpre com os requisitos de idade e saldo mensal necessários."));
    }
}
