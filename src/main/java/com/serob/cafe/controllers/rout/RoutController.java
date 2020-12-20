package com.serob.cafe.controllers.rout;

import com.serob.cafe.services.RoleUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;


/**
 * Controller to rout over templates
 */
@Controller
public class RoutController {

    private RoleUserService roleService;

    public RoutController(RoleUserService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping({"/", "/login"})
    public String index(){
        return "login";
    }

    @RequestMapping("/products")
    public String products(){
        return "products";
    }

    @RequestMapping("/users")
    public String users(){
        return "users";
    }

    @RequestMapping("/tables")
    public String tables(){
        return "tables";
    }
}
