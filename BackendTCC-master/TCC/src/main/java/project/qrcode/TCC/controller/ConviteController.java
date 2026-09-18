package project.qrcode.TCC.controller;

import project.qrcode.TCC.model.Convite;
import project.qrcode.TCC.service.ConviteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/convites")
public class ConviteController {

    private final ConviteService conviteService;

    public ConviteController(ConviteService conviteService) {
        this.conviteService = conviteService;
    }

    @PostMapping
    public ResponseEntity<Convite> criar(@RequestParam Long moradorId, @RequestParam String visitanteNome) {
        Convite convite = conviteService.criarConvite(moradorId, visitanteNome);
        return ResponseEntity.status(HttpStatus.CREATED).body(convite);
    }

    @GetMapping("/morador/{moradorId}")
    public ResponseEntity<List<Convite>> listarPorMorador(@PathVariable Long moradorId) {
        return ResponseEntity.ok(conviteService.listarPorMorador(moradorId));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            Convite convite = conviteService.cancelarConvite(id);
            return ResponseEntity.ok(convite);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}