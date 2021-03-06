package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ReportesController {
    //Inyección de dependencias
    @Autowired
    private ProductoProveedorService productoProveedorService;
    @Autowired
    private RequisicionDeArticuloService requisicionDeArticuloService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private ProductoRequisicionService productoRequisicionService;
    @Autowired
    private EscenarioCompraController escenarioCompraController;
    @Autowired
    private ProductoEmpresaService productoEmpresaService;
    @Autowired
    private OrdenDeCompraService ordenDeCompraService;

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
        //Precio con descuento
        Map<Long, Double> preciosConDescuento = escenarioCompraController.calcularPrecioConDescuento(productoProveedorTodos);
        /* FIN REPORTE DE PRECIOS VIGENTES DE ARTÍCULOS POR PROVEEDOR */

        //Manda los objetos a la vista

        //Se crea una lista de tipo DepartamentoMap para ingresar los id y nombre de todos los departamentos
        List<DepartamentoMap> departamentoMap = departamentoService.listDepartamentMap();
        int i = 0;
        //Acumulara las cantidades de productos solicitados
        int acumulador = 0;
        //Se recorreran todos los departamentos 1 a 1
        for (DepartamentoMap listaDepartamentoMap : departamentoMap) {

            //Se creara una lista de tipo Requisicion de articulo para tener todas las requisiciones  segun departamento
            List<RequisicionDeArticulo> listRequisicionDeArticulo = requisicionDeArticuloService.listSelectedbyDepartament(listaDepartamentoMap.getIdDeDepartamento());
            //Recorreremos todas las requisiciones del departamento que se recorre en el for externo
            for (RequisicionDeArticulo requisicion : listRequisicionDeArticulo) {
                //Se creara una lista de tipo Requisicion de articulo para tener todas las requisiciones  segun departamento
                List<ProductoRequisicion> listProductoRequisicion = productoRequisicionService.listadoPorId(requisicion.getIdRequisicionDeArticulo());

                for (ProductoRequisicion productoRequisicion : listProductoRequisicion) {
                    //Se le asignara a cada departamentoMap la cantidad de articulos que se pidieron.
                    acumulador = acumulador + productoRequisicion.getCantidad();
                }
            }
            departamentoMap.get(i).setNumeroDeVentas(acumulador);
            System.out.println(departamentoMap.get(i).toString());
            acumulador = 0;
            i ++;
        }

        System.out.println("Corrio: " + i);

        /* REPORTE STOCK INVENTARIO DE LA EMPRESA */
        List<ProductoEmpresa> listProductoEmpresaAll = productoEmpresaService.listAll();
        //Enviando los objetos a la vista
        mav.addObject("listProductoEmpresa", listProductoEmpresaAll);
        /* FIN REPORTE STOCK INVENTARIO DE LA EMPRESA */

        /* REPORTE ORDEN DE COMPRA POR PROVEEDOR */
        List<OrdenDeCompra> ordenesDeCompra = ordenDeCompraService.listAll();
        //Enviando los objetos a la vista
        mav.addObject("ordenesDeCompra", ordenesDeCompra);
        /* FIN REPORTE ORDEN DE COMPRA POR PROVEEDOR */

        //Enviando a la vista la lista de los productos por proveedor con su precio vigente
        mav.addObject("productoProveedorPrecioVigentes", productoProveedorPrecioVigentes);
        mav.addObject("departamentoMap", departamentoMap);
        mav.addObject("productoProveedorPrecioVigentes", productoProveedorTodos);
        mav.addObject("preciosConDescuento", preciosConDescuento);
        return mav;
    }


}
