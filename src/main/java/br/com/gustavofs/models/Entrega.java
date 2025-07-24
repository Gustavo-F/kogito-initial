package br.com.gustavofs.models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Entrega {

    @Id
    @GeneratedValue
    private UUID id;

    private String status;
    private String destinatario;
    // private Endereco endereco;
}
