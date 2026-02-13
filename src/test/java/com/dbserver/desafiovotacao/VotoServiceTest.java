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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
    public void naoDeveVotarEmSessaoEncerrada() {
        Long pautaId = 1L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta").build();
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now().minusMinutes(10))
                .dataEncerramento(LocalDateTime.now().minusMinutes(5))
                .build();

        when(cpfClient.validarCpf("123")).thenReturn(new CpfStatusDTO("ABLE_TO_VOTE"));
        when(sessaoService.buscarPorPauta(pautaId)).thenReturn(sessao);

        assertThrows(BusinessException.class, () -> {
            votoService.votar(pautaId, "123", Voto.OpcaoVoto.SIM);
        });
    }
}
