package com.sistemacompras.too.repository;

import com.sistemacompras.too.entity.Empleado;
import com.sistemacompras.too.entity.MovimientoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoEmpresaRepository extends JpaRepository<MovimientoEmpresa, Long > {

}
