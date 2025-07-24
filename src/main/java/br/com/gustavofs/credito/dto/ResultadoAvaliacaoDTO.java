package br.com.gustavofs.credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoAvaliacaoDTO {
    
    private Boolean aprovado;
    private String motivo;
}
