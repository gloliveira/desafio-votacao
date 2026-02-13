package com.dbserver.desafiovotacao.service;

import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository pautaRepository;

    @Transactional
    public Pauta cadastrar(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public Pauta buscarPorId(Long id) {
        return pautaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
    }

    public List<Pauta> listarTodas() {
        return pautaRepository.findAll();
    }
}
