package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/bodega")
public class RemisionController {
    @Autowired
    private DetalleOrdenDeCompraService detalleOrdenDeCompraService;
    @Autowired
    private OrdenDeCompraService ordenDeCompraService;
    @Autowired
    private ProductoEmpresaService service;
    @Autowired
    private NotaDeRemisionService notaDeRemisionService;
    @Autowired
    private MovimientoEmpresaService movimientoEmpresaService;
    @Autowired
    private NotaDeRemisionService notasService;

    //Metodo que lista las notas de remision
    @RequestMapping("/remisiones")
    public ModelAndView listarNotasRemision(){
        //Obteniendo todas las notas de remision
        List<NotasDeRemision> notasDeRemisions = notaDeRemisionService.listAll();
        ModelAndView mav = new ModelAndView("InventarioArticulos/listarNotasRemision");
        //Manda los objetos a la vista
        mav.addObject("notasDeRemisions", notasDeRemisions);
        return mav;
    }

    //Ver la remision recibiendo el id de la orden de compra ligada a dicha remision
    @RequestMapping("/remisiones/view/{id}")
    public ModelAndView showProductPage(@PathVariable(name = "id") Long id) {
        /**
         Aqui tengo que recibir el id de la orden de remision, y atraves del id de la orden
         de remision encontrar el id de la orden de compra y enviar a la vista la orden de
         compra :v
         */
        ModelAndView mav = new ModelAndView("InventarioArticulos/recibirArticulo.html");
        List<DetalleOrdenDeCompra> detalleOrdenDeCompra = detalleOrdenDeCompraService.listAllbyIdOrderCompra(id);
        for (DetalleOrdenDeCompra detalleOrdenDeCompra1 : detalleOrdenDeCompra)
        {
            System.out.println(detalleOrdenDeCompra1.toString());
        }
        //Se obtiene le id de la remision a partir de la orden de compra
        Long idRemision = notaDeRemisionService.getIdRemisionByIdOrdenDeCompra(id);

        //Se obtiene la nota de remision mediante el id de la remision
        NotasDeRemision notaDeRemision = notaDeRemisionService.get(idRemision);

        mav.addObject("notaDeRemision", notaDeRemision);
        mav.addObject("detalleOrdenDeCompra", detalleOrdenDeCompra);
        return mav;
    }

    //Cuando se acepta la orden de remision recibiendo el ide de la orden de compra
    @RequestMapping("/recib/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id){
        //Tenemos los detalleordenDeCompra que coincidan con el ir de la orden de empresa que recibimos
        List<DetalleOrdenDeCompra> detalleOrdenDeCompra = detalleOrdenDeCompraService.listAllbyIdOrderCompra(id);
       //Se listan TODOS los productos de la empresa.
        List<ProductoEmpresa> listProductoEmpresaAll = service.listAll();
        //Se crea una lista de productos de la empresa que luego seran guardados.
        ProductoEmpresa productosAGuardar = new ProductoEmpresa();


        String nombreProductoEntrante;
        String nombreProductoDeLaEmpresa;
        int i;

        for (DetalleOrdenDeCompra productoEntrante : detalleOrdenDeCompra) {
            //Aqui se le asigna a la variable nombreProductoEntrante el valor actual del objeto que se reccorre
            nombreProductoEntrante = productoEntrante.getIdProductoProveedor().getNombreProductoProveedor().toLowerCase();
            i = 0;

            for (ProductoEmpresa productoBD : listProductoEmpresaAll) {
                //Aqui se le asigna a la variable nombreProductoDeLaEmpresa el valor actual del objeto que se reccorre
                nombreProductoDeLaEmpresa = productoBD.getNombre().toLowerCase();
                /**
                 Aqui comparamos el producto que intenta ingresar a la empresa con todos los que ya
                 existen y estan en bd, si coincide con alguno es porquqe ya existe, e i suma una unidad
                 en tal caso ese producto ya existe y no se debe guardar en bd
                 */
                    if(nombreProductoEntrante.equals(nombreProductoDeLaEmpresa))
                    {
                        //Se toma la cantidad de productos que hay en bd y se le suma la cantidad entrante.
                        int nuevaCantidad = productoBD.getCantidad() + productoEntrante.getCantidad();
                        //Al producto de la bd se le suma lo nuevo
                      // productoBD.setCantidad(nuevaCantidad);
                        service.save(productoBD);
                        i++;
                        /*
                         * PROCESO PARA REGISTRAR UN MOVIMIENTO DE ENTRADA EN LA EMPRESA
                         * */
                        //Creando un objeto de tipo MovimientoEmpresa
                        MovimientoEmpresa movimientoEmpresaEntrada = new MovimientoEmpresa();
                        //Asigna el nombre del producto
                        movimientoEmpresaEntrada.setNombreProducto(nombreProductoEntrante);
                        //Asigna el tipo de movimiento
                        movimientoEmpresaEntrada.setMovimiento("Entrada al inventario");
                        //Agregando la fecha del movimiento
                        Date fechaMovimientoEntrada = new Date();
                        movimientoEmpresaEntrada.setFechaMovimiento(fechaMovimientoEntrada);
                        //Agregando la cantidad de entrada
                        movimientoEmpresaEntrada.setCantidadEntrada(productoEntrante.getCantidad());
                        //Agregando el precio de compra
                        movimientoEmpresaEntrada.setPrecioCompra(productoEntrante.getPrecio());
                        //Agregando el total de la compra
                        movimientoEmpresaEntrada.setTotal(productoEntrante.getCantidad() * productoEntrante.getPrecio());
                        //Guardando el movimiento de entrada de la empresa
                        movimientoEmpresaService.save(movimientoEmpresaEntrada);
                        /*
                         * PROCESO PARA REGISTRAR UN MOVIMIENTO DE SALIDA EN LA EMPRESA
                         * */
                        //Creando un objeto de tipo MovimientoEmpresa
                        MovimientoEmpresa movimientoEmpresaSalida = new MovimientoEmpresa();
                        //Asigna el nombre del producto
                        movimientoEmpresaSalida.setNombreProducto(nombreProductoEntrante);
                        //Asigna el tipo de movimiento
                        movimientoEmpresaSalida.setMovimiento("Salida del inventario");
                        //Agregando la fecha del movimiento
                        Date fechaMovimientoSalida = new Date();
                        movimientoEmpresaSalida.setFechaMovimiento(fechaMovimientoSalida);
                        //Agregando la cantidad de entrada
                        movimientoEmpresaSalida.setCantidadSalida(productoEntrante.getCantidad());
                        //Guardando el movimiento de entrada de la empresa
                        movimientoEmpresaService.save(movimientoEmpresaSalida);
                    }
            }
            //Si i vale 0 es porque ningun otro producto tiene ese nombre entonces se guarda
            if (i == 0)
            {
                //Esta cantidad se debe cambiar por la nueva
               // productosAGuardar.setCantidad(productoEntrante.getCantidad());
                productosAGuardar.setMax(200);
                productosAGuardar.setMin(150);
                productosAGuardar.setNombre(productoEntrante.getIdProductoProveedor().getNombreProductoProveedor());
                service.save(productosAGuardar);
                System.out.println("Guardo un producto de la empresa nuevo:");
                /*
                * PROCESO PARA REGISTRAR UN MOVIMIENTO DE ENTRADA EN LA EMPRESA
                * */
                //Creando un objeto de tipo MovimientoEmpresa
                MovimientoEmpresa movimientoEmpresaEntrada = new MovimientoEmpresa();
                //Asigna el nombre del producto
                movimientoEmpresaEntrada.setNombreProducto(nombreProductoEntrante);
                //Asigna el tipo de movimiento
                movimientoEmpresaEntrada.setMovimiento("Entrada al inventario");
                //Agregando la fecha del movimiento
                Date fechaMovimientoEntrada = new Date();
                movimientoEmpresaEntrada.setFechaMovimiento(fechaMovimientoEntrada);
                //Agregando la cantidad de entrada
                movimientoEmpresaEntrada.setCantidadEntrada(productoEntrante.getCantidad());
                //Agregando el precio de compra
                movimientoEmpresaEntrada.setPrecioCompra(productoEntrante.getPrecio());
                //Agregando el total de la compra
                movimientoEmpresaEntrada.setTotal(productoEntrante.getCantidad() * productoEntrante.getPrecio());
                //Guardando el movimiento de entrada de la empresa
                movimientoEmpresaService.save(movimientoEmpresaEntrada);
                /*
                 * PROCESO PARA REGISTRAR UN MOVIMIENTO DE SALIDA EN LA EMPRESA
                 * */
                //Creando un objeto de tipo MovimientoEmpresa
                MovimientoEmpresa movimientoEmpresaSalida = new MovimientoEmpresa();
                //Asigna el nombre del producto
                movimientoEmpresaSalida.setNombreProducto(nombreProductoEntrante);
                //Asigna el tipo de movimiento
                movimientoEmpresaSalida.setMovimiento("Salida del inventario");
                //Agregando la fecha del movimiento
                Date fechaMovimientoSalida = new Date();
                movimientoEmpresaSalida.setFechaMovimiento(fechaMovimientoSalida);
                //Agregando la cantidad de entrada
                movimientoEmpresaSalida.setCantidadSalida(productoEntrante.getCantidad());
                //Guardando el movimiento de entrada de la empresa
                movimientoEmpresaService.save(movimientoEmpresaSalida);
            }
        }

        //Se obtiene le id de la remision a partir de la orden de compra
        Long idRemision = notaDeRemisionService.getIdRemisionByIdOrdenDeCompra(id);

        //Se obtiene la nota de remision mediante el id de la remision
        NotasDeRemision notaDeRemision = notaDeRemisionService.get(idRemision);
        notaDeRemision.setEstado(1);
        notaDeRemisionService.save(notaDeRemision);

        return "redirect:/bodega/remisiones";
    }

}
