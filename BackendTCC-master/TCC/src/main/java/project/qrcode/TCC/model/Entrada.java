package project.qrcode.TCC.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "convite_id")
    private Long conviteId;

    @Column(name = "porteiro_id", nullable = false)
    private Long porteiroId;

    @Column(name = "token_informado", nullable = false, length = 36)
    private String tokenInformado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEntrada status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_validacao", nullable = false, length = 20)
    private TipoValidacao tipoValidacao;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public Entrada() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConviteId() {
        return conviteId;
    }

    public void setConviteId(Long conviteId) {
        this.conviteId = conviteId;
    }

    public Long getPorteiroId() {
        return porteiroId;
    }

    public void setPorteiroId(Long porteiroId) {
        this.porteiroId = porteiroId;
    }

    public String getTokenInformado() {
        return tokenInformado;
    }

    public void setTokenInformado(String tokenInformado) {
        this.tokenInformado = tokenInformado;
    }

    public StatusEntrada getStatus() {
        return status;
    }

    public void setStatus(StatusEntrada status) {
        this.status = status;
    }

    public TipoValidacao getTipoValidacao() {
        return tipoValidacao;
    }

    public void setTipoValidacao(TipoValidacao tipoValidacao) {
        this.tipoValidacao = tipoValidacao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}