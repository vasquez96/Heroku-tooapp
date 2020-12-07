package com.sistemacompras.too.repository;

import com.sistemacompras.too.entity.NotasDeRemision;
import com.sistemacompras.too.entity.OrdenDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRemisionRepository extends JpaRepository<NotasDeRemision, Long> {
}
