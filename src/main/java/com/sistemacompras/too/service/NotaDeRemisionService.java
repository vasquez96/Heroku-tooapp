package com.sistemacompras.too.service;

import com.sistemacompras.too.entity.NotasDeRemision;
import com.sistemacompras.too.entity.OrdenDeCompra;
import com.sistemacompras.too.entity.RequisicionDeArticulo;
import com.sistemacompras.too.repository.NotaRemisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotaDeRemisionService {
    //Inyeccion de dependencias
    @Autowired
    private NotaRemisionRepository notaDeRemisionRepository;
    @Autowired
    private NotaDeRemisionService notaDeRemisionService;

    //Muestra todas las ordenes de compra
    public List<NotasDeRemision> listAll() {
        return notaDeRemisionRepository.findAll();
    }

    //Guarda una orden de compra
    public void save(NotasDeRemision notasDeRemision){
        notaDeRemisionRepository.save(notasDeRemision);
    }

    //Obtiene una orden de compra por su id
    public NotasDeRemision get(Long id){
        return notaDeRemisionRepository.findById(id).get();
    }
    //Elimina una orden de compra por su id
    public void delete(Long id){
        notaDeRemisionRepository.deleteById(id);
    }

    //Devuelve las remisiones segun su estado
    public List<NotasDeRemision> listSelected(int estado) {
        //Se crea una lista con todas las remisiones
        List<NotasDeRemision> listNotasDeRemisionall = notaDeRemisionService.listAll();

        //Se crea lista para ingresar las remisiones que sean del estado solicitado
        List<NotasDeRemision> listRemisiones = new ArrayList();

        for (NotasDeRemision remision : listNotasDeRemisionall) {

            if(remision.getEstado() == estado)
            {
                listRemisiones.add(remision);
            }
        }

        return listRemisiones;
    }

    public Long getIdRemisionByIdOrdenDeCompra(Long idOrdenDeCompra)
    {
        //Se crea una lista con todas las remisiones
        List<NotasDeRemision> listNotasDeRemisionall = notaDeRemisionService.listAll();
        Long idRemision = Long.valueOf(0);
        //Se recorren todas las notas de remision
        for (NotasDeRemision remision : listNotasDeRemisionall) {

            //Se compara el id de la orden de compra.
            if(remision.getIdOrdenDeCompra().getIdOrdenDeCompra() == idOrdenDeCompra)
            {
                idRemision = remision.getIdNotaRemision();
                return  idRemision;
            }
        }


        return  idRemision;
    }


}
