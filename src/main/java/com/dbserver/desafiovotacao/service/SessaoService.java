package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.exception.BusinessException;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.SessaoVotacao;
import com.dbserver.desafiovotacao.repository.SessaoVotacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoService {
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final PautaService pautaService;

    @Transactional
    public SessaoVotacao abrirSessao(Long pautaId, Long minutos) {
        Pauta pauta = pautaService.buscarPorId(pautaId);
        
        sessaoVotacaoRepository.findByPautaId(pautaId).ifPresent(s -> {
            throw new BusinessException("Já existe uma sessão para esta pauta");
        });

        long duracao = (minutos == null || minutos <= 0) ? 1 : minutos;
        
        SessaoVotacao sessao = SessaoVotacao.builder()
                .pauta(pauta)
                .dataAbertura(LocalDateTime.now())
                .dataEncerramento(LocalDateTime.now().plusMinutes(duracao))
                .build();

        return sessaoVotacaoRepository.save(sessao);
    }

    public SessaoVotacao buscarPorPauta(Long pautaId) {
        return sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new BusinessException("Sessão não encontrada para a pauta informada"));
    }

    @Transactional
    public void fecharSessaoManualmente(Long pautaId) {
        SessaoVotacao sessao = buscarPorPauta(pautaId);
        if (!sessao.estaAberta()) {
            throw new BusinessException("A sessão já está fechada");
        }
        sessao.setDataEncerramento(LocalDateTime.now());
        sessaoVotacaoRepository.save(sessao);
    }
}
