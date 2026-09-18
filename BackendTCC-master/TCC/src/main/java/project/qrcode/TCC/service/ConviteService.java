package project.qrcode.TCC.service;

import project.qrcode.TCC.model.Convite;
import project.qrcode.TCC.model.StatusConvite;
import project.qrcode.TCC.repository.ConviteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ConviteService {

    private final ConviteRepository conviteRepository;

    public ConviteService(ConviteRepository conviteRepository) {
        this.conviteRepository = conviteRepository;
    }

    public Convite criarConvite(Long moradorId, String visitanteNome) {
        LocalDateTime agora = LocalDateTime.now();
        Convite convite = new Convite();
        convite.setMoradorId(moradorId);
        convite.setVisitanteNome(visitanteNome);
        convite.setToken(UUID.randomUUID().toString());
        convite.setStatus(StatusConvite.ATIVO);
        convite.setDataCriacao(agora);
        convite.setDataExpiracao(agora.plusHours(24));
        return conviteRepository.save(convite);
    }

    public List<Convite> listarPorMorador(Long moradorId) {
        return conviteRepository.findByMoradorIdOrderByDataCriacaoDesc(moradorId);
    }

    public Convite cancelarConvite(Long id) {
        Convite convite = conviteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Convite não encontrado com id " + id));
        convite.setStatus(StatusConvite.REVOGADO);
        return conviteRepository.save(convite);
    }
}