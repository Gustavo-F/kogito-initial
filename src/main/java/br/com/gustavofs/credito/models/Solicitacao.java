package br.com.gustavofs.credito.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    
    private Cliente cliente;
    private BigDecimal rendaMensal;
    private BigDecimal gastoMensal;
    private BigDecimal valorSolicitado;
    private LocalDateTime dataHoraSolicitacao;
}
