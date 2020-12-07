package com.sistemacompras.too.service;

import com.sistemacompras.too.entity.ProductoRequisicion;
import com.sistemacompras.too.repository.ProductoRequisicionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoRequisicionService {
    //Inyeccion de dependencias
    @Autowired
    private ProductoRequisicionRepository productoRequisicionRepository;
	@Autowired
    private ProductoRequisicionService productoRequisicionService;

    //Muestra todos los productos
    public List<ProductoRequisicion> listAll() {
        return productoRequisicionRepository.findAll();
    }
    //Guarda un producto
    public void save(ProductoRequisicion productoRequisicion){
        productoRequisicionRepository.save(productoRequisicion);
    }
    //Obtiene un producto por su id
    public ProductoRequisicion get(Long id){
        return productoRequisicionRepository.findById(id).get();
    }
    //Elimina un producto por su id
    public void delete(Long id){
        productoRequisicionRepository.deleteById(id);
    }
    //Buscar listado por id
    public List<ProductoRequisicion> listByIdRequisicion(Long id) {
        return productoRequisicionRepository.findAllByIdRequisicionDeArticulo(id);
    }

    //Listar todas los productosRequisicion segun el id de la requisicion
    public List<ProductoRequisicion> listadoPorId(Long id) {
    	List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listAll();
        List<ProductoRequisicion> list = new ArrayList<>();
        
        for (ProductoRequisicion nombre : productoRequisicion) {
         	if (nombre.getIdRequisicionDeArticuloEnLong() == id)         	
         	    list.add(nombre);       
        	}
        return list;
    }
}
