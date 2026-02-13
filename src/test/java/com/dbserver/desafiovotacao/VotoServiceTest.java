package com.dbserver.desafiovotacao;

import com.dbserver.desafiovotacao.client.CpfClient;
import com.dbserver.desafiovotacao.dto.CpfStatusDTO;
import com.dbserver.desafiovotacao.exception.BusinessException;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.SessaoVotacao;
import com.dbserver.desafiovotacao.model.Voto;
import com.dbserver.desafiovotacao.repository.VotoRepository;
import com.dbserver.desafiovotacao.service.SessaoService;
import com.dbserver.desafiovotacao.service.VotoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;
    @Mock
    private SessaoService sessaoService;
    @Mock
    private CpfClient cpfClient;

    @InjectMocks
    private VotoService votoService;

    @Test
    @DisplayName("Não deve permitir voto quando a sessão já expirou em relação ao tempo atual")
    public void naoDeveVotarEmSessaoEncerrada() {
        // Arrange
        Long pautaId = 1L;
        String cpfAleatorio = "12345678901";
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta Teste").build();
        
        // Criando uma sessão que encerrou há 1 segundo (dinâmico)
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now().minusMinutes(2))
                .dataEncerramento(LocalDateTime.now().minusSeconds(1)) 
                .build();

        when(cpfClient.validarCpf(cpfAleatorio)).thenReturn(new CpfStatusDTO("ABLE_TO_VOTE"));
        when(sessaoService.buscarPorPauta(pautaId)).thenReturn(sessao);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            votoService.votar(pautaId, cpfAleatorio, Voto.OpcaoVoto.SIM);
        }, "Deveria lançar exceção pois a sessão encerrou dinamicamente");
    }

    @Test
    @DisplayName("Deve barrar voto de associado não habilitado pelo serviço externo")
    public void deveBarrarCpfNaoHabilitado() {
        String cpfInvalido = "00000000000";
        when(cpfClient.validarCpf(cpfInvalido)).thenReturn(new CpfStatusDTO("UNABLE_TO_VOTE"));

        assertThrows(BusinessException.class, () -> {
            votoService.votar(1L, cpfInvalido, Voto.OpcaoVoto.SIM);
        });
    }
}
