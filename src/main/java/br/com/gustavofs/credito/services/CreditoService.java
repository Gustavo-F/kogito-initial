package br.com.gustavofs.credito.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.gustavofs.credito.dto.ResultadoAvaliacaoDTO;
import br.com.gustavofs.credito.dto.SolicitacaoCreditoDTO;
import br.com.gustavofs.credito.models.Cliente;
import br.com.gustavofs.credito.models.ResultadoSolicitacao;
import br.com.gustavofs.credito.models.Solicitacao;

@Service
public class CreditoService {
    
    public Solicitacao solicitarCredito(SolicitacaoCreditoDTO dto) {
        return Solicitacao.builder()
            .cliente(new Cliente(dto.getNomeCliente(), dto.getDataNascimentoCliente()))
            .rendaMensal(dto.getRendaMensal())
            .gastoMensal(dto.getGastoMensal())
            .valorSolicitado(dto.getValorSolicitado())
            .dataHoraSolicitacao(LocalDateTime.now())
            .build();
    }

    public ResultadoSolicitacao obterResultadoSolicitacao(ResultadoAvaliacaoDTO dto, Solicitacao solicitacao) {
        return ResultadoSolicitacao.builder()
            .nomeCliente(solicitacao.getCliente().getNome())
            .descricao(dto.getMotivo())
            .valor(solicitacao.getValorSolicitado())
            .status(dto.getAprovado() ? "APROVADO" : "REPROVADO")
            .dataHoraResultado(LocalDateTime.now())
            .build();
    }
}
