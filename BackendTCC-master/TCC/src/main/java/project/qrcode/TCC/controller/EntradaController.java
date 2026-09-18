package project.qrcode.TCC.controller;


import project.qrcode.TCC.model.Entrada;
import project.qrcode.TCC.service.EntradaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @PostMapping("/validar")
    public ResponseEntity<String> validar(@RequestParam String token, @RequestParam Long porteiroId) {
        EntradaService.ResultadoValidacao resultado = entradaService.validarAcesso(token, porteiroId);
        if (resultado.isSucesso()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultado.getMensagem());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultado.getMensagem());
    }

    @GetMapping
    public ResponseEntity<Page<Entrada>> historico(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        return ResponseEntity.ok(entradaService.listarHistorico(pagina, tamanho));
    }
}