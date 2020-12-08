package com.sistemacompras.too.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sistemacompras.too.entity.ProductoProveedor;
import com.sistemacompras.too.service.ProductoProveedorService;
import com.sistemacompras.too.entity.Proveedor;
import com.sistemacompras.too.entity.User;
import com.sistemacompras.too.service.ProveedorService;
import com.sistemacompras.too.service.UserService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/proveedor")
public class ProductoProveedorController {

	//Controlador de Producto de proveedor
    @Autowired
    private ProductoProveedorService service;
    @Autowired
    private ProveedorService ProveedorService;
    @Autowired
    private UserService userService;

    @RequestMapping("/ProductoProveedor")
    public String viewHomePage(Model model, HttpServletRequest request){
        //Guardamos el username del usuario activo  en la variable username
    	String username = request.getUserPrincipal().getName();
    	
        //Obtenemos todos los objetos de tipo Producto proveedor
        List<ProductoProveedor> listProductoProveedorAll = service.listAll();
        List<ProductoProveedor> listProductoProveedor = new ArrayList();
        
        //Se le asigna a userId el id de usuario que tiene la cuenta activa.
    	Long userId = userService.getIdByUsername(username);
    	//Se obtiene el idProveedor por el idUser
    	Long idProveedor = ProveedorService.getidByUserId(userId);
    	
        for (ProductoProveedor productoProveedor : listProductoProveedorAll) {
        	//Si el idProveedor coinciden se añadiran a la nueva lista
        	if(productoProveedor.getIdProveedor().getIdProveedor().toString().equals(idProveedor.toString()))
        	{
        		listProductoProveedor.add(productoProveedor);
        	}
  
        }
    	System.out.println("el username del usuario activo es: " + username);
    	System.out.println("el id del usuario activo es: " + userId);
    	System.out.println("el id de proveedor del usuario activo es: " + idProveedor);
   
        model.addAttribute("listProductoProveedor", listProductoProveedor);
        return "ProductoProveedor/index"; //Nombre del html
    }
    
    //Ruta para un nuevo producto proveedor
    @RequestMapping("/ProductoProveedor/new")
    public String showNewProductForm(Model model){
        //Creando una instancia de Producto proveedor
    	ProductoProveedor productoProveedor = new ProductoProveedor();
        model.addAttribute("productoProveedor", productoProveedor);
        return "ProductoProveedor/new";
    }
    
    //Ruta para crear un nuevo producto
    @RequestMapping(value = "/ProductoProveedor/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("productoProveedor") ProductoProveedor productoProveedor, HttpServletRequest request, RedirectAttributes redirAttrs){
        //Guardamos el username del usuario activo  en la variable username
    	String username = request.getUserPrincipal().getName();
        //Se le asigna a userId el id de usuario que tiene la cuenta activa.
    	Long userId = userService.getIdByUsername(username);
    	//Se le da como parametro el id de usuasrio y se obtiene el id de proveedor
    	Long idProveedor = ProveedorService.getidByUserId(userId);
    	
    	Proveedor proveedor = ProveedorService.get(idProveedor);
    	productoProveedor.setIdProveedor(proveedor);
        service.save(productoProveedor);

            redirAttrs.addFlashAttribute("message", "Producto guardado correctamente");
            return "redirect:/proveedor/ProductoProveedor" ;
        }

    
    //Editar un producto de proveedor
    @RequestMapping("/ProductoProveedor/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") Long id){
        ModelAndView mav = new ModelAndView("ProductoProveedor/edit");
        ProductoProveedor productoProveedor = service.get(id);
        mav.addObject("productoProveedor", productoProveedor);
        return mav;
    }
    
    //View un producto de proveedor
    @RequestMapping("/ProductoProveedor/view/{id}")
    public ModelAndView showProductPage(@PathVariable(name = "id") Long id){
        ModelAndView mav = new ModelAndView("ProductoProveedor/view");
        ProductoProveedor productoProveedor = service.get(id);
        mav.addObject("productoProveedor", productoProveedor);
        return mav;
    }
    
    //Eliminar un producto de proveedor
    @RequestMapping("/ProductoProveedor/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id,RedirectAttributes redirAttrs){
        service.delete(id);
        redirAttrs.addFlashAttribute("message", "Producto eliminado correctamente");
        return "redirect:/proveedor/ProductoProveedor" ;
    }
}
