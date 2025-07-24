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
public class ResultadoSolicitacao { 

    private String nomeCliente;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime dataHoraResultado;
    private String status;
}
