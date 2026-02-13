package com.dbserver.desafiovotacao;

import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.repository.PautaRepository;
import com.dbserver.desafiovotacao.service.PautaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    public void deveCadastrarPautaComSucesso() {
        Pauta pauta = Pauta.builder().titulo("Teste").build();
        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        Pauta cadastrada = pautaService.cadastrar(pauta);

        assertEquals("Teste", cadastrada.getTitulo());
    }
}
