package br.com.gustavofs.credito.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private String nome;
    private String dataNascimento;    
}
