package com.sistemacompras.too.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemacompras.too.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long > {

}
