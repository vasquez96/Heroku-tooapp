package com.sistemacompras.too.service;

import com.sistemacompras.too.entity.RequisicionDeArticulo;
import com.sistemacompras.too.repository.RequisicionDeArticuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequisicionDeArticuloService {
    //Inyeccion de dependencias
    @Autowired
    private RequisicionDeArticuloRepository requisicionDeArticuloRepository;
    @Autowired
    private RequisicionDeArticuloService requisicionDeArticuloService;

    //Muestra todos los productos
    public List<RequisicionDeArticulo> listAll() {
        return requisicionDeArticuloRepository.findAll();
    }

    //Devuelve las notas segun su estado
    public List<RequisicionDeArticulo> listSelected(int estado) {
        //Se crea una lista con todas las requisiciones
        List<RequisicionDeArticulo> listRequisicionDeArticuloall = requisicionDeArticuloService.listAll();

        //Se crea lista para ingresar las requisiciones que sean del estado solicitado
        List<RequisicionDeArticulo> listRequisicionDeArticulo = new ArrayList();

        for (RequisicionDeArticulo requisicionDeArticulo : listRequisicionDeArticuloall) {

            if(requisicionDeArticulo.getEstado() == estado)
            {
                listRequisicionDeArticulo.add(requisicionDeArticulo);
            }
        }

        return listRequisicionDeArticulo;
    }



    //Guarda un producto
    public void save(RequisicionDeArticulo requisicionDeArticulo){
        requisicionDeArticuloRepository.save(requisicionDeArticulo);
    }
    //Obtiene un producto por su id
    public RequisicionDeArticulo get(Long id){
        return requisicionDeArticuloRepository.findById(id).get();
    }
    //Elimina un producto por su id
    public void delete(Long id){
        requisicionDeArticuloRepository.deleteById(id);
    }


    //Devuelve las notas segun el departamento al que pertenezcan
    public List<RequisicionDeArticulo> listSelectedbyDepartament(Long departamento) {
        //Se crea una lista con todas las requisiciones
        List<RequisicionDeArticulo> listRequisicionDeArticuloall = requisicionDeArticuloService.listAll();

        //Se crea lista para ingresar las requisiciones que sean del departamento solicitado
        List<RequisicionDeArticulo> listRequisicionDeArticulo = new ArrayList();

        for (RequisicionDeArticulo requisicionDeArticulo : listRequisicionDeArticuloall) {
            if(requisicionDeArticulo.getIdDepartamento().getIdDepartamento() == departamento)
            {
                listRequisicionDeArticulo.add(requisicionDeArticulo);
            }
        }
        return listRequisicionDeArticulo;
    }
}
