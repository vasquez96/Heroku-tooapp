package com.sistemacompras.too.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller

public class AppController {
    @GetMapping({"/", "login"})
    public String index() {
        return "login";
    }

//    @GetMapping("/menu2")
//    public String menu() {
//        return "dashboardAdmin";
//    }

    @GetMapping("/admin")
    public String menu() {
        return "dashboardAdmin";
    }

    @GetMapping("/jefe")
    public String jefeDashboard() {
        return "RequisicionJefeDepartamento/index";
    }

    @GetMapping("/empleado")
    public String empleadoDashboard() {
        return "EmpleadoDepartamentoCompras/dashboardEmpleado";
    }

    @GetMapping("/proveedor")
    public ModelAndView proveedorDashboard() {
        return new ModelAndView("dashboardProveedor");
    }

    @GetMapping("/bodega")
    public ModelAndView bodegueroDashboard() {
        return new ModelAndView("dashboardBodeguero");
    }
}
//
//    @GetMapping("/user")
//    public String user(){
//        return "user";
//    }
//
//    @GetMapping("/admin")
//    public String admin(){
//        return "admin";
//    }







