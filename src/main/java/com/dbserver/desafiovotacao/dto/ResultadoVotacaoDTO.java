package com.dbserver.desafiovotacao.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultadoVotacaoDTO {
    private Long pautaId;
    private String titulo;
    private Long votosSim;
    private Long votosNao;
    private String resultado;
}
