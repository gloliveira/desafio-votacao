package com.dbserver.desafiovotacao.dto;

import com.dbserver.desafiovotacao.model.Voto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoRequest {
    @NotBlank(message = "O ID do associado é obrigatório")
    private String associadoId;
    
    @NotNull(message = "O voto (SIM/NAO) é obrigatório")
    private Voto.OpcaoVoto voto;
}
