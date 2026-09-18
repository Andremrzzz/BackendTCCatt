package project.qrcode.TCC.service;


import project.qrcode.TCC.model.Convite;
import project.qrcode.TCC.model.Entrada;
import project.qrcode.TCC.model.StatusConvite;
import project.qrcode.TCC.model.StatusEntrada;
import project.qrcode.TCC.model.TipoValidacao;
import project.qrcode.TCC.repository.ConviteRepository;
import project.qrcode.TCC.repository.EntradaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EntradaService {

    private final ConviteRepository conviteRepository;
    private final EntradaRepository entradaRepository;

    public EntradaService(ConviteRepository conviteRepository, EntradaRepository entradaRepository) {
        this.conviteRepository = conviteRepository;
        this.entradaRepository = entradaRepository;
    }

    @Transactional
    public ResultadoValidacao validarAcesso(String token, Long porteiroId) {
        Optional<Convite> conviteOpt = conviteRepository.findByToken(token);
        if (conviteOpt.isEmpty()) {
            registrarEntrada(null, porteiroId, token, StatusEntrada.NEGADO);
            return new ResultadoValidacao(false, "Token inválido: convite não encontrado.");
        }

        Convite convite = conviteOpt.get();
        if (convite.getStatus() == StatusConvite.REVOGADO) {
            registrarEntrada(convite.getId(), porteiroId, token, StatusEntrada.NEGADO);
            return new ResultadoValidacao(false, "Acesso negado: este convite foi revogado pelo morador.");
        }

        if (convite.getStatus() == StatusConvite.UTILIZADO) {
            registrarEntrada(convite.getId(), porteiroId, token, StatusEntrada.NEGADO);
            return new ResultadoValidacao(false, "Acesso negado: este QR Code já foi utilizado.");
        }

        boolean expirado = convite.getStatus() == StatusConvite.EXPIRADO
                || convite.getDataExpiracao().isBefore(LocalDateTime.now());
        if (expirado) {
            convite.setStatus(StatusConvite.EXPIRADO);
            conviteRepository.save(convite);
            registrarEntrada(convite.getId(), porteiroId, token, StatusEntrada.NEGADO);
            return new ResultadoValidacao(false, "Acesso negado: este QR Code expirou.");
        }

        convite.setStatus(StatusConvite.UTILIZADO);
        conviteRepository.save(convite);
        registrarEntrada(convite.getId(), porteiroId, token, StatusEntrada.LIBERADO);
        return new ResultadoValidacao(true, "Acesso liberado com sucesso.");
    }

    public Page<Entrada> listarHistorico(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by("dataHora").descending());
        return entradaRepository.findAll(pageable);
    }

    private void registrarEntrada(Long conviteId, Long porteiroId, String tokenInformado, StatusEntrada status) {
        Entrada entrada = new Entrada();
        entrada.setConviteId(conviteId);
        entrada.setPorteiroId(porteiroId);
        entrada.setTokenInformado(tokenInformado);
        entrada.setStatus(status);
        entrada.setTipoValidacao(TipoValidacao.QR_CODE);
        entrada.setDataHora(LocalDateTime.now());
        entradaRepository.save(entrada);
    }

    public static class ResultadoValidacao {
        private final boolean sucesso;
        private final String mensagem;

        public ResultadoValidacao(boolean sucesso, String mensagem) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
        }

        public boolean isSucesso() {
            return sucesso;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}