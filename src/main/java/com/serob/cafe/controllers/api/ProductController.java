package com.serob.cafe.controllers.api;

import com.serob.cafe.entities.Product;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.ProductRepository;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.services.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductRepository productRepo;
    private UserRepository userRepo;
    private RoleUserService roleService;

    @Autowired
    public ProductController(ProductRepository productRepo, UserRepository userRepo, RoleUserService roleUserService){
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.roleService = roleUserService;
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@Validated @RequestBody Product product, @RequestParam("manager_id") Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        ResponseEntity badResponse = roleService.mustBeManager(userOptional);
        if (badResponse != null) {
            return badResponse;
        }
        //see create Table comment.
        //see create Table comment.
        User user = userOptional.get();
        product.setUser(user);
        Product createdProduct = productRepo.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping()
    public ResponseEntity<Iterable<Product>> getAllProducts(){
        Iterable<Product> allProducts = productRepo.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }
}