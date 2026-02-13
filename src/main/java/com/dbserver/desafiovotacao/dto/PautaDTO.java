package com.dbserver.desafiovotacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PautaDTO {
    private Long id;
    
    @NotBlank(message = "O título é obrigatório")
    private String titulo;
    
    private String descricao;
}
