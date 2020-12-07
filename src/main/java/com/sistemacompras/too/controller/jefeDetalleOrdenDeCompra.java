package com.sistemacompras.too.controller;


import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/jefe")
public class jefeDetalleOrdenDeCompra {
    @Autowired
    private DetalleOrdenDeCompraService detalleOrdenDeCompraService;
    @Autowired
    private OrdenDeCompraService ordenDeCompraService;

    //Ver la orden de compra
    @RequestMapping("/ordenesDeCompras/view/{id}")
    public ModelAndView showProductPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("EmpleadoDepartamentoCompras/verOrdenDeCompra");
        List<DetalleOrdenDeCompra> detalleOrdenDeCompra = detalleOrdenDeCompraService.listAllbyIdOrderCompra(id);
        for (DetalleOrdenDeCompra detalleOrdenDeCompra1 : detalleOrdenDeCompra) {
            System.out.println(detalleOrdenDeCompra1.toString());
        }

        mav.addObject("detalleOrdenDeCompra", detalleOrdenDeCompra);

        return mav;
    }

    //Listar las ordenes de compra
    @RequestMapping("/ordenesDeCompras")
    public String viewHomePage(Model model) {

        //se crea una lista y se le asignan las requisiciones aprobadas, para eso es el metodo listSelected y el 1 para las aprobadas
        List<OrdenDeCompra> ordenDeCompras = ordenDeCompraService.listAll();
        model.addAttribute("listordenDeCompras", ordenDeCompras);

        return "EmpleadoDepartamentoCompras/listadoOrdenCompras.html"; //Nombre del html
    }
}
