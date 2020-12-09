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


@Controller
@RequestMapping("/admin")
public class AdministrarRequisicionController {

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
    private ProductoEmpresaService productoEmpresaServiceService;

    //Listar las requisiciones pendientes = 0.
    @RequestMapping("/requisicionAdmin")
    public ModelAndView viewHomePage(HttpServletRequest request) {
    //se crea una lista y se le asignan las requisiciones aprobadas, para eso es el metodo listSelected y el 0 para las pendientes
        List<RequisicionDeArticulo> listRequisicionDeArticulo = requisicionDeArticuloService.listSelected(0);
        ModelAndView mav = new ModelAndView("RequisicionAdminDepartamento/adminRequisicion"); //Nombre del html
        mav.addObject("listRequisicionDeArticulo", listRequisicionDeArticulo);
        return mav;
    }

    //View un producto de proveedor
    @RequestMapping("/requisicionAdmin/aceptarDenegarAdmin/{id}")
    public ModelAndView showProductPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("RequisicionAdminDepartamento/adminAceptarDenegar");
        List<ProductoRequisicion> productoRequisicion = productoRequisicionService.listadoPorId(id);
        List<ProductoEmpresa> productoEmpresa = productoEmpresaServiceService.listAll();
        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);

        mav.addObject("productoEmpresa", productoEmpresa);
        mav.addObject("productoRequisicion", productoRequisicion);
        mav.addObject("requisicionArticulo", requisicionDeArticulo);
        return mav;
    }

    //Metodo que camiba el estado y la observacion al aceptar o denegar la requisicion
    @RequestMapping(value = "/requisicionAdmin/editar/", method = RequestMethod.POST)
    public String editarRequisicion(
            @RequestParam(value = "boton") String aprobarDenegar,
            @RequestParam(name = "observaciones") String observaciones,
            @RequestParam(name = "isss") Long id,
            HttpServletRequest request) {
        System.out.println("Id de la requisicio: " + id);
        System.out.println("boton: " + aprobarDenegar);

        RequisicionDeArticulo requisicionDeArticulo = requisicionDeArticuloService.get(id);

        //Guardamos el username del usuario activo  en la variable username
        String username = request.getUserPrincipal().getName();
        //Se le asigna a userId el id de usuario que tiene la cuenta activa.
        Long userId = userService.getIdByUsername(username);
        //Se le da como parametro el id de usuasrio y se obtiene el id de empleado
        Long idEmpleado = empleadoService.getidByUserId(userId);
        // Se crea un objeto de tipo empleado y se llena con el metodo get
        Empleado empleado = empleadoService.get(idEmpleado);
        //A la requisicion que se esta creando se le asigna el nombre y apellido de quien lo elabora
        String autorizadoPor = empleado.getNombreEmpleado() + " " + empleado.getApellidoEmpleado();

        int estado;
        if(aprobarDenegar.equals("Aceptar")) {
            estado = 1;
            requisicionDeArticulo.setAutorizadoPor(autorizadoPor);
        }
        else
            estado = 2;

        requisicionDeArticulo.setEstado(estado);
        requisicionDeArticulo.setObservacion(observaciones);
        System.out.println("Hola mundo: " + requisicionDeArticulo.toString());

        requisicionDeArticuloService.save(requisicionDeArticulo);
        return "redirect:/admin/requisicionAdmin";
    }
}
