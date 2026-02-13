package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.exception.BusinessException;
import com.dbserver.desafiovotacao.model.SessaoVotacao;
import com.dbserver.desafiovotacao.model.Voto;
import com.dbserver.desafiovotacao.repository.VotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class VotoService {
    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;
    private final com.dbserver.desafiovotacao.client.CpfClient cpfClient;

    @Transactional
    public Voto votar(Long pautaId, String associadoId, Voto.OpcaoVoto opcao) {
        log.info("Processando voto do associado {} na pauta {}", associadoId, pautaId);
        com.dbserver.desafiovotacao.dto.CpfStatusDTO cpfStatus = cpfClient.validarCpf(associadoId);
        if ("UNABLE_TO_VOTE".equals(cpfStatus.getStatus())) {
            throw new BusinessException("Associado não está habilitado para votar");
        }

        SessaoVotacao sessao = sessaoService.buscarPorPauta(pautaId);

        if (!sessao.estaAberta()) {
            throw new BusinessException("A sessão de votação está encerrada");
        }

        votoRepository.findByPautaIdAndAssociadoId(pautaId, associadoId).ifPresent(v -> {
            throw new BusinessException("Associado já votou nesta pauta");
        });

        Voto voto = Voto.builder()
                .pauta(sessao.getPauta())
                .associadoId(associadoId)
                .voto(opcao)
                .dataVoto(LocalDateTime.now())
                .build();

        return votoRepository.save(voto);
    }

    public Map<Voto.OpcaoVoto, Long> contabilizarVotos(Long pautaId) {
        List<Voto> votos = votoRepository.findAllByPautaId(pautaId);
        return votos.stream()
                .collect(Collectors.groupingBy(Voto::getVoto, Collectors.counting()));
    }
}
