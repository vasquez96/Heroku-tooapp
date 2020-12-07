package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.NotasDeRemision;
import com.sistemacompras.too.entity.ProductoProveedor;
import com.sistemacompras.too.service.ProductoProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ReportesController {
    //Inyección de dependencias
    @Autowired
    private ProductoProveedorService productoProveedorService;

    //Metodo que muestra los reportes
    @RequestMapping("/reportes")
    public ModelAndView reportes(){
        ModelAndView mav = new ModelAndView("Reportes/reportes");
        /* REPORTE DE PRECIOS VIGENTES DE ARTÍCULOS POR PROVEEDOR */
        //Declaramos un objeto de tipo Date el cual captura la fecha de hoy
        Date fechaActual = new Date();
        //Obtenemos todos los productos de los proveedores
        List<ProductoProveedor> productoProveedorTodos = productoProveedorService.listAll();
        //Creamos una lista que almacenará los productos de los proveedores que se encuentren en el rango
        // de la fecha de hoy y no haya terminado la promoción
        List<ProductoProveedor> productoProveedorPrecioVigentes = null;
        for (int i = 0; i < productoProveedorTodos.size(); i++) {
            if (fechaActual.after(productoProveedorTodos.get(i).getFechaVigenciaInicio()) && fechaActual.before(productoProveedorTodos.get(i).getFechaVigenciaFinal())) {
                productoProveedorPrecioVigentes.add((ProductoProveedor) productoProveedorTodos);
            }
        }


        //Manda los objetos a la vista

        //Enviando a la vista la lista de los productos por proveedor con su precio vigente
        mav.addObject("productoProveedorPrecioVigentes", productoProveedorPrecioVigentes);
        return mav;
    }
}
