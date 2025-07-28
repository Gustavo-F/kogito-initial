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

import br.com.gustavofs.credito.dto.SolicitacaoCreditoDTO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class SolicitacaoCreditoBPMNTest {

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
        SolicitacaoCreditoDTO solicitacao = SolicitacaoCreditoDTO.builder()
            .nomeCliente("John Doe")
            .dataNascimentoCliente(LocalDate.now().minusYears(15).toString())
            .rendaMensal(BigDecimal.valueOf(2500.00))
            .gastoMensal(BigDecimal.valueOf(1500.00))
            .valorSolicitado(BigDecimal.valueOf(5000))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("solicitacaoRequest", solicitacao))
            .when()
                .post("/solicitacao_credito")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("resultadoSolicitacao.nomeCliente", equalTo(solicitacao.getNomeCliente()))
                .body("resultadoSolicitacao.status", equalTo("REPROVADO"))
                .body("resultadoSolicitacao.descricao", equalTo("Cliente deve ser maior de idade para a obtenção de crédito."));
        
    }

    @Test
    public void testQuandoSaldoMensalNegativo() {
        SolicitacaoCreditoDTO solicitacao = SolicitacaoCreditoDTO.builder()
            .nomeCliente("John Doe")
            .dataNascimentoCliente(LocalDate.now().minusYears(45).toString())
            .rendaMensal(BigDecimal.valueOf(2500.00))
            .gastoMensal(BigDecimal.valueOf(2600.00))
            .valorSolicitado(BigDecimal.valueOf(5000))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("solicitacaoRequest", solicitacao))
            .when()
                .post("/solicitacao_credito")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("resultadoSolicitacao.nomeCliente", equalTo(solicitacao.getNomeCliente()))
                .body("resultadoSolicitacao.status", equalTo("REPROVADO"))
                .body("resultadoSolicitacao.descricao", equalTo("Cliente possui saldo mensal zerado ou negativo."));
    }

    @Test
    public void testQuandoSaldoMensalInsuficiente() {
        SolicitacaoCreditoDTO solicitacao = SolicitacaoCreditoDTO.builder()
            .nomeCliente("John Doe")
            .dataNascimentoCliente(LocalDate.now().minusYears(45).toString())
            .rendaMensal(BigDecimal.valueOf(2500.00))
            .gastoMensal(BigDecimal.valueOf(2000.00))
            .valorSolicitado(BigDecimal.valueOf(5000))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("solicitacaoRequest", solicitacao))
            .when()
                .post("/solicitacao_credito")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("resultadoSolicitacao.nomeCliente", equalTo(solicitacao.getNomeCliente()))
                .body("resultadoSolicitacao.status", equalTo("REPROVADO"))
                .body("resultadoSolicitacao.descricao", equalTo("Saldo mensal do cliente é incompatível com o valor solicitado."));
    }

    @Test
    public void testQuandoCreditoAprovado() {
        SolicitacaoCreditoDTO solicitacao = SolicitacaoCreditoDTO.builder()
            .nomeCliente("John Doe")
            .dataNascimentoCliente(LocalDate.now().minusYears(45).toString())
            .rendaMensal(BigDecimal.valueOf(5000.00))
            .gastoMensal(BigDecimal.valueOf(2000.00))
            .valorSolicitado(BigDecimal.valueOf(10000))
            .build();

        RestAssured.given()
                .spec(specification)
                .body(Map.of("solicitacaoRequest", solicitacao))
            .when()
                .post("/solicitacao_credito")
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("resultadoSolicitacao.nomeCliente", equalTo(solicitacao.getNomeCliente()))
                .body("resultadoSolicitacao.status", equalTo("APROVADO"))
                .body("resultadoSolicitacao.descricao", equalTo("O cliente cumpre com os requisitos de idade e saldo mensal necessários."));
    }
}