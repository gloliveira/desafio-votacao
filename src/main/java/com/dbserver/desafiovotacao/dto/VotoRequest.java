package com.dbserver.desafiovotacao.dto;

import com.dbserver.desafiovotacao.model.Voto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VotoRequest {
    @NotBlank(message = "O identificador do associado (CPF) é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    private String associadoId;
    
    @NotNull(message = "O voto é obrigatório e deve ser 'SIM' ou 'NAO'")
    private Voto.OpcaoVoto voto;
}
