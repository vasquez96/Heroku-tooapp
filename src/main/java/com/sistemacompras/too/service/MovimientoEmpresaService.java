package com.sistemacompras.too.service;

import com.sistemacompras.too.entity.Empleado;
import com.sistemacompras.too.entity.MovimientoEmpresa;
import com.sistemacompras.too.entity.NotasDeRemision;
import com.sistemacompras.too.repository.EmpleadoRepository;
import com.sistemacompras.too.repository.MovimientoEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovimientoEmpresaService {
    //Inyeccion de dependencias
    @Autowired
    private MovimientoEmpresaRepository movimientoEmpresaRepository;

    //Muestra todas los movimientos de la empresa
    public List<MovimientoEmpresa> listAll() {
        return movimientoEmpresaRepository.findAll();
    }

    //Guarda un movimiento de la empresa
    public void save(MovimientoEmpresa movimientoEmpresa){
        movimientoEmpresaRepository.save(movimientoEmpresa);
    }

    //Obtiene un movimiento de la empresa por medio de su id
    public MovimientoEmpresa get(Long id){
        return movimientoEmpresaRepository.findById(id).get();
    }
    //Elimina un movimineto de la empresa por medio de su id
    public void delete(Long id){
        movimientoEmpresaRepository.deleteById(id);
    }

}
