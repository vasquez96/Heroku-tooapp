package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.*;
import com.sistemacompras.too.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class MovimientoInventarioEmpresaController {
	
	@Autowired
    private MovimientoEmpresaService movimientoEmpresaService;

    //Ver las requisiciones.
    @RequestMapping("/movimientoInventario")
    public ModelAndView showProductPage(){
        ModelAndView mav = new ModelAndView("InventarioArticulos/movimientoInventario");
        //Obteniendo todos los movimientos
        List<MovimientoEmpresa> movimientoEmpresas = movimientoEmpresaService.listAll();
       // System.out.println(requisicionDeArticulo.toString());
        mav.addObject("movimientoEmpresas", movimientoEmpresas);
        return mav;
    }

}
