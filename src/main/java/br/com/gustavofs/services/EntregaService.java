package br.com.gustavofs.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gustavofs.models.Entrega;
import br.com.gustavofs.repositories.EntregaRepository;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public Entrega cadastrarEntrega(Entrega entrega) {
        // Entrega entregaCadastrada = Entrega.builder()
        // .id(UUID.randomUUID())
        // .status(entrega.getStatus())
        // .destinatario(entrega.getDestinatario())
        // .build();

        // return entregaCadastrada;

        return entregaRepository.save(entrega);
    }

    public Entrega buscarEntregaPorId(String entregaId) {
        return entregaRepository.findById(UUID.fromString(entregaId))
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada com o ID: " + entregaId));
    }
}
