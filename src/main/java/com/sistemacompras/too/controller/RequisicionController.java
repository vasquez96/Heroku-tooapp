package com.sistemacompras.too.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/jefe")
public class RequisicionController {
	
	@Autowired
    private ProductoProveedorService productoService;
    @Autowired
    private RequisicionDeArticuloService requisicionDeArticuloService;
	@Autowired
    private ProductoRequisicionService productoRequisicionService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private UserService userService;
    @Autowired
    private com.sistemacompras.too.service.ProveedorService ProveedorService;

	//Metodo que lleva a la vista de crear requisicion
    @GetMapping("/requisicion/crear")
    public String menuAdmin(Model model) {
    	List<ProductoProveedor> listProductos = productoService.listAll();
    	ProductoRequisicion productoRequisicion = new ProductoRequisicion();
    	String x, y;
        for(int j = 0; j < listProductos.size(); j++){ //Inicio ciclo for
            for(int i = 0; i < listProductos.size(); i++){ //Inicio ciclo for
                if(i != j) 
                {
                	x = listProductos.get(i).getNombreProductoProveedor().toLowerCase();
                	y = listProductos.get(j).getNombreProductoProveedor().toLowerCase();
                	//listProductos.get(i).getNombreProductoProveedor().equals(listProductos.get(j).getNombreProductoProveedor())
                	System.out.println(y + " vs " + x + " Posicion: " + j + "\n");                		
                	if (x.equals(y))
            		{
            			listProductos.remove(j);
            		}
                }
            }
        }
        
        for(int i = 0; i < listProductos.size(); i++){ //Inicio ciclo for
        	listProductos.get(i).setNombreProductoProveedor(listProductos.get(i).getNombreProductoProveedor().toLowerCase());
        }

    	model.addAttribute("listProductos", listProductos);
    	model.addAttribute("productoRequisicion", productoRequisicion);
        return "RequisicionJefeDepartamento/crearRequisicion";
    }

    //Listar las requisiciones segun el departamento al que pertenezca el usuario.
    @GetMapping("/requisicion")
    public String viewHomePage(Model model,  HttpServletRequest request){
        //Guardamos el username del usuario activo  en la variable username
        String username = request.getUserPrincipal().getName();
        //Obtenemos el objeto empleado apartir del username del usuario activo
        Empleado empleado = empleadoService.getEmpleadoByUsername(username);

        //Se crea una lista con todoas las requisiciones
        List<RequisicionDeArticulo> listRequisicionDeArticuloall = requisicionDeArticuloService.listAll();
        //Se crea lista para ingresar las requisiciones que sean del departamento de quien tiene la sesion activa
        List<RequisicionDeArticulo> listRequisicionDeArticulo = new ArrayList();

        for (RequisicionDeArticulo requisicionDeArticulo : listRequisicionDeArticuloall) {
            //Si coinciden entraran a la nueva lista
            if(requisicionDeArticulo.getIdDepartamento().getIdDepartamento().toString().equals(empleado.getIdDepartamento().getIdDepartamento().toString()))
            {
                listRequisicionDeArticulo.add(requisicionDeArticulo);
            }

        }
        model.addAttribute("listRequisicionDeArticulo", listRequisicionDeArticulo);
        return "RequisicionJefeDepartamento/index"; //Nombre del html
    }

    //Metodo que guarda una requisicion
    @RequestMapping(value = "/requisicion/save", method = RequestMethod.POST)
    public String guardarRequisicion(
            @RequestParam(name = "cantidad") ArrayList<Integer> cantidad,
            @RequestParam(name = "articulo") ArrayList<Long> articulo,
            HttpServletRequest request, RedirectAttributes redirAttrs
    ) {

        //Verifica si la cantidad o los articulos estan vacios
        if (cantidad.isEmpty() || articulo.isEmpty()){
            redirAttrs.addFlashAttribute("message", "No pueden haber valores vacíos");
            return "redirect:/jefe/requisicion/crear";
        }

        //Verifica si existe un valor que sea cero en la cantidad
        int valorDeCantidad = 0;
        for (int i = 0; i < cantidad.size(); i++) {
            if (cantidad.get(i) == 0){
                valorDeCantidad++;
            }
        }
        if (valorDeCantidad > 0){
            redirAttrs.addFlashAttribute("message", "El valor de la cantidad no puede ser cero");
            return "redirect:/jefe/requisicion/crear";
        }

        //Verifica si existe un valor negativo en la cantidad
        int valorNegativo = 0;
        for (int i = 0; i < cantidad.size(); i++) {
            if (cantidad.get(i) < 0){
                valorNegativo++;
            }
        }
        if (valorNegativo > 0){
            redirAttrs.addFlashAttribute("message", "El valor de la cantidad no puede ser negativa");
            return "redirect:/jefe/requisicion/crear";
        }

        //Verifica si existe un valor del articulo que sea vacio
        int articuloVacio = 0;
        for (int i = 0; i < articulo.size(); i++) {
            if (articulo.get(i) == null){
                articuloVacio++;
            }
        }
        if (articuloVacio > 0){
            redirAttrs.addFlashAttribute("message", "Uno o más artículos se encuentran vacíos");
            return "redirect:/jefe/requisicion/crear";
        } else {

            //Creando una instancia de fecha para capturar la fecha del hoy
            Date fecha = new Date();
            //Creando una requisicion de articulo
            RequisicionDeArticulo requisicionDeArticulo = new RequisicionDeArticulo();
            //Modificando la fecha de la elaboracion de la requisicion
            requisicionDeArticulo.setFechaPedido(fecha);
            //Guardamos el username del usuario activo  en la variable username
            String username = request.getUserPrincipal().getName();
            //Se le asigna a userId el id de usuario que tiene la cuenta activa.
            Long userId = userService.getIdByUsername(username);
            //Se le da como parametro el id de usuasrio y se obtiene el id de empleado
            Long idEmpleado = empleadoService.getidByUserId(userId);
            // Se crea un objeto de tipo empleado y se llena con el metodo get
            Empleado empleado = empleadoService.get(idEmpleado);

            //A la requisicion que se esta creando se le asigna un empleado
            requisicionDeArticulo.setIdEmpleado(empleado);
            //A la requisicion que se esta creando se le asigna el nombre y apellido de quien lo elabora
            String elaboradoPor = empleado.getNombreEmpleado() + " " + empleado.getApellidoEmpleado();
            requisicionDeArticulo.setElaboradoPor(elaboradoPor);
            requisicionDeArticulo.setIdDepartamento(empleado.getIdDepartamento());


            //Se guarda la requisicion
            requisicionDeArticuloService.save(requisicionDeArticulo);
            //System.out.println("Datos de cantidad: " + cantidad.size());
            //System.out.println("Datos de articulo: " + articulo.size());
            //Ciclo que recorre la cantidad de datos solicitdados para la requisicion
            for (int i = 0; i < cantidad.size(); i++) { //Inicio ciclo for
                //System.out.println("Valor de la cantidad del articulo: " + cantidad.get(i));
                //System.out.println("Nombre de articulo: " + productoService.get(articulo.get(i)));
                //Creando una instancia de producto requisicion
                ProductoRequisicion productoRequisicion = new ProductoRequisicion();
                ProductoProveedor productoProveedor = productoService.get(articulo.get(i));
                //System.out.println("Nombre de articulo: " + productoProveedor.getNombreProductoProveedor());
                //Agrengando la cantidad pedida del articulo
                productoRequisicion.setCantidad(cantidad.get(i));
                //Agregando el producto solicitado
                productoRequisicion.setIdProductoProveedor(productoProveedor);
                //Agregando la requisicion
                productoRequisicion.setIdRequisicionDeArticulo(requisicionDeArticulo);
                //System.out.println("DATOS DE PRODUCTO REQUISICION: " + productoRequisicion.toString());
                //Guardando los productos de la requisicion
                productoRequisicionService.save(productoRequisicion);
            } //Fin ciclo for
            redirAttrs.addFlashAttribute("message", "Requisición generada con éxito");
            return "redirect:/jefe/requisicion";
        } //Fin del else
    }

    //Ver las requisiciones.
    @RequestMapping("/requisicion/view/{id}")
    public ModelAndView showProductPage(@PathVariable(name = "id") Long id){
        ModelAndView mav = new ModelAndView("RequisicionJefeDepartamento/view");
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);
        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);

        for (ProductoRequisicion nombre : productoRequisicion) {
            System.out.println(nombre.toString());    
        	}
         
       // System.out.println(requisicionDeArticulo.toString());
        mav.addObject("productoRequisicion", productoRequisicion);
        mav.addObject("requisicionArticulo", requisicionDeArticulo);
        return mav;
    }

    //Editar las requisiciones.
    @RequestMapping("/requisicion/edit/{id}")
    public ModelAndView editarRequisicion(@PathVariable(name = "id") Long id, Model model){
        ModelAndView mav = new ModelAndView("RequisicionJefeDepartamento/editarRequisicion");
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);

        //Obteniendo los productos por medio del id de la requisicion
        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);

        for (ProductoRequisicion nombre : productoRequisicion) {
            System.out.println(nombre.toString());
        }
        List<ProductoProveedor> listProductos = productoService.listAll();
        String x, y;
        for(int j = 0; j < listProductos.size(); j++){ //Inicio ciclo for
            for(int i = 0; i < listProductos.size(); i++){ //Inicio ciclo for
                if(i != j)
                {
                    x = listProductos.get(i).getNombreProductoProveedor().toLowerCase();
                    y = listProductos.get(j).getNombreProductoProveedor().toLowerCase();
                    //listProductos.get(i).getNombreProductoProveedor().equals(listProductos.get(j).getNombreProductoProveedor())
                    System.out.println(y + " vs " + x + " Posicion: " + j + "\n");
                    if (x.equals(y))
                    {
                        listProductos.remove(j);
                    }
                }
            }
        }

        for(int i = 0; i < listProductos.size(); i++){ //Inicio ciclo for
            listProductos.get(i).setNombreProductoProveedor(listProductos.get(i).getNombreProductoProveedor().toLowerCase());
        }

        model.addAttribute("listProductos", listProductos);
        model.addAttribute("idReq", id);
        // System.out.println(requisicionDeArticulo.toString());
        mav.addObject("productoRequisicion", productoRequisicion);
        mav.addObject("requisicionDeArticulo", requisicionDeArticulo);
        return mav;
    }
    //Eliminar requisicion
    @RequestMapping("/requisicion/eliminar/{id}")
    public String deleteRequisicion(@PathVariable(name = "id") Long id,RedirectAttributes redirAttrs){

        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);

        for (ProductoRequisicion proreq : productoRequisicion) {
            if (proreq.getIdRequisicionDeArticuloEnLong() == id)
                productoRequisicionService.delete(proreq.getIdProductoRequisicion());
        }

        requisicionDeArticuloService.delete(id);
        redirAttrs.addFlashAttribute("message", "Requisición eliminada con éxito");
        return "redirect:/jefe/requisicion";
    }

    //Metodo que guarda la requisicion editada
    @RequestMapping(value = "/requisicion/editar/", method = RequestMethod.POST)
    public String editarRequisicion(
            @RequestParam(name = "cantidad") ArrayList<Integer> cantidad,
            @RequestParam(name = "articulo") ArrayList<Long> articulo,
            @RequestParam(name = "isss") Long id,RedirectAttributes redirAttrs) {
        System.out.println("Id de la requisicio: " + id);
        System.out.println("Buscando por id-------- ");
        //Buscando los productos de la requisicion que corresponden al id de la requisicion
        List<ProductoRequisicion> listaProductoRequisicion = productoRequisicionService.listadoPorId(id);
        List<ProductoRequisicion> auxiliarListaProductoRequisicion = listaProductoRequisicion;
        System.out.println("Eliminando articulo ----------");
        for(int i = 0; i < listaProductoRequisicion.size(); i++){
            System.out.println(listaProductoRequisicion.get(i).toString());
            productoRequisicionService.delete(listaProductoRequisicion.get(i).getIdProductoRequisicion());
        }
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);
        //Ciclo que recorre la cantidad de datos solicitados para la requisicion
        for(int i = 0; i < cantidad.size(); i++){ //Inicio ciclo for
//            if(listaProductoRequisicion.get(i).getIdProductoProveedor().toString() == productoService.get(articulo.get(i)).getIdProductoProveedor().toString()){
//                listaProductoRequisicion.get(i).setCantidad(cantidad.get(i));
//            }
            //Creando una instancia de producto requisicion
            ProductoRequisicion productoRequisicion = new ProductoRequisicion();
            ProductoProveedor productoProveedor = productoService.get(articulo.get(i));
            //Agrengando la cantidad pedida del articulo
            productoRequisicion.setCantidad(cantidad.get(i));
            //Agregando el producto solicitado
            productoRequisicion.setIdProductoProveedor(productoProveedor);
            //Guardando los productos de la requisicion
            //Agregando la requisicion
            productoRequisicion.setIdRequisicionDeArticulo(requisicionDeArticulo);
            requisicionDeArticulo.setEstado(0);
            requisicionDeArticulo.setObservacion("");
            productoRequisicionService.save(productoRequisicion);
        } //Fin ciclo for
        redirAttrs.addFlashAttribute("message", "Requisición editada con éxito");
        return "redirect:/jefe/requisicion";
    }



}
