package project.qrcode.TCC.repository;


import project.qrcode.TCC.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    // findAll(Pageable) já vem herdado de JpaRepository e é usado no histórico paginado.
}