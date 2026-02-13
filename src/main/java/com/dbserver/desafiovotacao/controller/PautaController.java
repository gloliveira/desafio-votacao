package com.dbserver.desafiovotacao.controller;

import com.dbserver.desafiovotacao.dto.*;
import com.dbserver.desafiovotacao.model.Pauta;
import com.dbserver.desafiovotacao.model.Voto;
import com.dbserver.desafiovotacao.service.PautaService;
import com.dbserver.desafiovotacao.service.SessaoService;
import com.dbserver.desafiovotacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas e votações")
public class PautaController {

    private final PautaService pautaService;
    private final SessaoService sessaoService;
    private final VotoService votoService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova pauta")
    public ResponseEntity<Pauta> cadastrar(@Valid @RequestBody PautaDTO pautaDTO) {
        Pauta pauta = Pauta.builder()
                .titulo(pautaDTO.getTitulo())
                .descricao(pautaDTO.getDescricao())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(pautaService.cadastrar(pauta));
    }

    @GetMapping
    @Operation(summary = "Listar todas as pautas")
    public ResponseEntity<List<Pauta>> listar() {
        return ResponseEntity.ok(pautaService.listarTodas());
    }

    @PostMapping("/{id}/sessao")
    @Operation(summary = "Abrir uma sessão de votação em uma pauta")
    public ResponseEntity<String> abrirSessao(@PathVariable Long id, @RequestBody(required = false) SessaoRequest request) {
        Long minutos = (request != null) ? request.getMinutos() : null;
        sessaoService.abrirSessao(id, minutos);
        return ResponseEntity.ok("Sessão aberta com sucesso");
    }

    @PostMapping("/{id}/votos")
    @Operation(summary = "Receber votos dos associados em pautas")
    public ResponseEntity<String> votar(@PathVariable Long id, @Valid @RequestBody VotoRequest request) {
        votoService.votar(id, request.getAssociadoId(), request.getVoto());
        return ResponseEntity.ok("Voto registrado com sucesso");
    }

    @GetMapping("/{id}/resultado")
    @Operation(summary = "Contabilizar os votos e dar o resultado da votação")
    public ResponseEntity<ResultadoVotacaoDTO> obterResultado(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPorId(id);
        Map<Voto.OpcaoVoto, Long> contagem = votoService.contabilizarVotos(id);
        
        long sim = contagem.getOrDefault(Voto.OpcaoVoto.SIM, 0L);
        long nao = contagem.getOrDefault(Voto.OpcaoVoto.NAO, 0L);
        
        String resultado = "EMPATE";
        if (sim > nao) resultado = "APROVADO";
        else if (nao > sim) resultado = "REPROVADO";

        ResultadoVotacaoDTO dto = ResultadoVotacaoDTO.builder()
                .pautaId(id)
                .titulo(pauta.getTitulo())
                .votosSim(sim)
                .votosNao(nao)
                .resultado(resultado)
                .build();

        return ResponseEntity.ok(dto);
    }
}
