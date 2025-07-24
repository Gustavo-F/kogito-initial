package br.com.gustavofs.credito.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCreditoDTO {
    
    private String nomeCliente;
    private String dataNascimentoCliente;
    private BigDecimal rendaMensal;
    private BigDecimal gastoMensal;
    private BigDecimal valorSolicitado;
}
