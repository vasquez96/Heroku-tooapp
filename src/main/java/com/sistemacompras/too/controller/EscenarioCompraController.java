package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/empleado")
public class EscenarioCompraController {

    //Inyección de dependencias
    @Autowired
    private RequisicionDeArticuloService requisicionDeArticuloService;
    @Autowired
    private ProductoRequisicionService productoRequisicionService;
    @Autowired
    private ProductoProveedorService productoProveedorService;
    @Autowired
    private OrdenDeCompraService ordenDeCompraService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private DetalleOrdenDeCompraService detalleOrdenDeCompraService;

    //Método que muestra la requisicion aprobada para iniciar un escenario de compra
    @RequestMapping("/requisicion/view/{id}")
    public ModelAndView showRequisicionAprobada(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("EmpleadoDepartamentoCompras/view");
        //Obtiene la requisicion de articulo por medio del id
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);
        //Obtiene la lista de los productos de la requisicion por medio del id de la requisicion
        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);
        //Manda los objetos a la vista
        mav.addObject("productoRequisicion", productoRequisicion);
        mav.addObject("requisicionArticulo", requisicionDeArticulo);
        return mav;
    }//Fin del metodo showRequisicionAprobada

    //Método que muestra el escenario de compra
    @RequestMapping("/escenario/{id}")
    public ModelAndView showEscenarioCompra(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("EmpleadoDepartamentoCompras/escenarioDeCompra");
        //Obtiene la requisicion de articulo por medio del id
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);
        //Obtiene la lista de los productos de la requisicion por medio del id de la requisicion
        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);
        //Obtiene la lista de los productos de los diferentes proveedores
        List<ProductoProveedor> listProductoProveedor = productoProveedorService.listAll();
        //Manda los objetos a la vista
        mav.addObject("productoRequisicion", productoRequisicion);
        mav.addObject("requisicionArticulo", requisicionDeArticulo);
        mav.addObject("listProductoProveedor", listProductoProveedor);
        return mav;
    }//Fin del metodo showEscenarioCompra

    //Método que genera la orden de compra
    @RequestMapping(value = "/ordenCompra/save", method = RequestMethod.POST)
    public String guardarRequisicion(@RequestParam(name = "idProductoProveedor") ArrayList<Long> idProductoProveedor) {
        System.out.println("\n**************CANTIDAD DE DATOS: " + idProductoProveedor.size());
        //Lista que almacena los id de los proveedores
        ArrayList<Long> idProveedores = new ArrayList<>();
        //Lista que almacena los productos de los proveedores
        ArrayList<ProductoProveedor> productosProveedores = new ArrayList<>();
        //Creando una instancia de tipo ProductoProveedor
        ProductoProveedor productoProveedor;
        for (int i = 0; i < idProductoProveedor.size(); i++) {
            System.out.println("*****Producto " + i + ": ");
            //System.out.println(productoProveedorService.get(idProductoProveedor.get(i)));
            //Agregando los productos a la lista productosProveedores por medio del id
            productosProveedores.add(productoProveedorService.get(idProductoProveedor.get(i)));
            System.out.println("***Producto: " + productosProveedores.get(i));
            //Agrengando el id del proveedor al ArrayList de idProveedores
            idProveedores.add(Long.parseLong(productosProveedores.get(i).getIdProveedor().getIdProveedor().toString()));
            System.out.println("***Id proveedor :" + idProveedores.get(i));
        }

        //Pasamos la lista a un stream ya que nos ofrece el metodo distinct el cual elimina los duplicados y retorna un stream
        //Luego agrupamos el stream y lo volcamos en una lista nuevamente.
        idProveedores = (ArrayList<Long>) idProveedores.stream().distinct().collect(Collectors.toList());
        //Imprimimos la lista utilizando la referencia al metodo println
        idProveedores.forEach(System.out::println);
        
        //Lista que almacena los productos de los proveedores y que servirá para representar el valor en el map
        ArrayList<ProductoProveedor> productosPorPreveedor;
        //Estructura de datos que almacena el id del proveedor junto con sus productos (clave/valor)
        Map<Long, ArrayList<ProductoProveedor>> ordenDeCompra = new HashMap<>();

        //Vamos a separar todos los productos y los vamos a asignar junto al proveedor que corresponde cada producto
        for (Long idProveedor: idProveedores) { //Inicio del foreach
            productosPorPreveedor = new ArrayList<>(); //Limpia la lista de productosPorProveedor
            for (int j = 0; j < productosProveedores.size(); j++) {
                //Verifica si el id del proveedor corresponde al id del proveedor que se encuentra dentro del producto
                if (idProveedor == Long.parseLong(productosProveedores.get(j).getIdProveedor().getIdProveedor().toString())){
                    productosPorPreveedor.add(productosProveedores.get(j)); //Agrega el producto a la listaproductosPorProveedor
                    //System.out.println("***Hello!");
                }
            }
            ordenDeCompra.put(idProveedor, productosPorPreveedor); //Agregando el proveedor y sus productos (clave/valor)
            //for(Map.Entry<Long, ArrayList<ProductoProveedor> >ordenCompra : ordenDeCompra.entrySet()){
                //System.out.println(ordenCompra.getKey() + " : " + ordenCompra.getValue());
            //}
        } //Fin del ciclor foreach

        //Imprimiendo ordenDeCompra
        for(Map.Entry<Long, ArrayList<ProductoProveedor> >ordenCompra : ordenDeCompra.entrySet()){
            System.out.println(ordenCompra.getKey() + " : " + ordenCompra.getValue());
        }

        /*
        * Proceso de creación de una orden de compra
        * */
        for(Map.Entry<Long, ArrayList<ProductoProveedor> >ordenCompra : ordenDeCompra.entrySet()){
            System.out.println(ordenCompra.getKey() + " : " + ordenCompra.getValue());
            //Variable que nos permitirá crear una nueva orden de compra de acuerdo a la cantidad de proveedores
            OrdenDeCompra ordenDe_Compra = new OrdenDeCompra();
            //Creando un objeto de tipo Date para almacenar la fecha en que se realizó la orden de compra
            Date fechaPedido = new Date();
            //Agregando la fecha a la orden de compra
            ordenDe_Compra.setFechaPedido(fechaPedido);
            //Obtenemos el proveedor por medio de su id y lo asignamos a la orden de compra
            ordenDe_Compra.setIdProveedor(proveedorService.get(ordenCompra.getKey()));
            //Guardamos la orden de compra
            ordenDeCompraService.save(ordenDe_Compra);
            /*
            *Una vez guardada la orden de compra procedemos a crear el detalle de la orden de compra
            */

            //Ciclo que recorre la cantidad productos que tendrá cada proveedor en su orden de compra
            for (int i = 0; i < ordenCompra.getValue().size(); i++) {
                //Creamos un objeto de tipo DetalleOrdenDeCompra
                DetalleOrdenDeCompra detalleOrdenDeCompra = new DetalleOrdenDeCompra();
                //Asignamos la orden de compra que acabamos de crear a detalle orden de compra
                detalleOrdenDeCompra.setIdOrdenDeCompra(ordenDe_Compra);
                //Asignado el producto del proveedor a detalle orden de compra
                detalleOrdenDeCompra.setIdProductoProveedor(ordenCompra.getValue().get(i));
                //Guardando el detalle de la orden de compra
                detalleOrdenDeCompraService.save(detalleOrdenDeCompra);
            }//Fin del ciclo for
            //System.out.println("****ID ORDEN COMPRA: " + ordenDe_Compra.getIdOrdenDeCompra().toString());
        }//Fin de la creacion de una orden de compra





//        if (bindingResult.hasErrors()) {
//            return "RequisicionJefeDepartamento/crearRequisicion";
//        } else {
        //Creando una instancia de fecha para capturar la fecha del hoy
        ////Date fecha = new Date();
        //Creando una requisicion de articulo
        ////RequisicionDeArticulo requisicionDeArticulo = new RequisicionDeArticulo();
        //Modificando la fecha de la elaboracion de la requisicion
        ////requisicionDeArticulo.setFechaPedido(fecha);
        ///requisicionDeArticuloService.save(requisicionDeArticulo);
        //System.out.println("Datos de cantidad: " + cantidad.size());
        //System.out.println("Datos de articulo: " + articulo.size());
        //Ciclo que recorre la cantidad de datos solicitdados para la requisicion
        ////for (int i = 0; i < cantidad.size(); i++) { //Inicio ciclo for
        //System.out.println("Valor de la cantidad del articulo: " + cantidad.get(i));
        //System.out.println("Nombre de articulo: " + productoService.get(articulo.get(i)));
        //Creando una instancia de producto requisicion
        ////ProductoRequisicion productoRequisicion = new ProductoRequisicion();
        ////ProductoProveedor productoProveedor = productoService.get(articulo.get(i));
        //System.out.println("Nombre de articulo: " + productoProveedor.getNombreProductoProveedor());
        //Agrengando la cantidad pedida del articulo
        ////productoRequisicion.setCantidad(cantidad.get(i));
        //Agregando el producto solicitado
        ////productoRequisicion.setIdProductoProveedor(productoProveedor);
        //Agregando la requisicion
        ////productoRequisicion.setIdRequisicionDeArticulo(requisicionDeArticulo);
        //System.out.println("DATOS DE PRODUCTO REQUISICION: " + productoRequisicion.toString());
        //Guardando los productos de la requisicion
        ////productoRequisicionService.save(productoRequisicion);
        ////} //Fin ciclo for
        return "redirect:/empleado";
    }

    //Listar las requisiciones aprobadas = 1.
    @RequestMapping("/requisicionAprobada")
    public String viewHomePageA(Model model, HttpServletRequest request) {

        //se crea una lista y se le asignan las requisiciones aprobadas, para eso es el metodo listSelected y el 1 para las aprobadas
        List<RequisicionDeArticulo> listRequisicionDeArticulo = requisicionDeArticuloService.listSelected(1);
        model.addAttribute("listRequisicionDeArticulo", listRequisicionDeArticulo);

        return "EmpleadoDepartamentoCompras/index.html"; //Nombre del html
    }
}