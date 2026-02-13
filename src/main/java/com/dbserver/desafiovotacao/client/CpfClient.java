package com.dbserver.desafiovotacao.client;

import com.dbserver.desafiovotacao.dto.CpfStatusDTO;
import com.dbserver.desafiovotacao.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CpfClient {

    private final Random random = new Random();

    /**
     * Simula a validação de um CPF em um serviço externo.
     * Tarefa Bônus 1 - Integração com sistemas externos.
     */
    public CpfStatusDTO validarCpf(String cpf) {
        // Simula o comportamento aleatório conforme requisito do desafio
        int result = random.nextInt(3); 

        if (result == 0) {
            // Simula 404 (CPF Inválido)
            throw new BusinessException("CPF inválido ou não encontrado");
        } else if (result == 1) {
            return new CpfStatusDTO("ABLE_TO_VOTE");
        } else {
            return new CpfStatusDTO("UNABLE_TO_VOTE");
        }
    }
}
