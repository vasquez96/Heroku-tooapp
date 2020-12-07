package com.sistemacompras.too.controller;

import com.sistemacompras.too.entity.ProductoProveedor;
import com.sistemacompras.too.entity.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.sistemacompras.too.entity.ProductoEmpresa;
import com.sistemacompras.too.service.ProductoEmpresaService;


@Controller
@RequestMapping("/bodega")
public class ProductoEmpresaController {
    //Controlador de Producto de empresa
    @Autowired
    private ProductoEmpresaService service;

    @RequestMapping("/productoEmpresa")
    public String viewHomePage(Model model, HttpServletRequest request){
        List<ProductoEmpresa> listProductoEmpresaAll = service.listAll();

        model.addAttribute("listProductoEmpresa", listProductoEmpresaAll);
        return "InventarioArticulos/inventarioEmpresa.html"; //Nombre del html
    }

    //Editar un producto de empresa
    @RequestMapping("/productoEmpresa/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") Long id){
        ModelAndView mav = new ModelAndView("InventarioArticulos/EditarArticulos");
        ProductoEmpresa productoEmpresa = service.get(id);
        mav.addObject("productoEmpresa", productoEmpresa);
        return mav;
    }

    //Ruta para guarda la edicion
    @RequestMapping(value = "/productoEmpresa/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("productoEmpresa") ProductoEmpresa productoEmpresa, HttpServletRequest request){

        service.save(productoEmpresa);
        return "redirect:/bodega/productoEmpresa";
    }
}
