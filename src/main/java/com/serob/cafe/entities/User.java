package com.serob.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serob.cafe.utils.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class User extends BaseEntity{

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties({"assignedTo", "createdBy",})
    private List<CafeTable> createdTables = new ArrayList<>();

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnoreProperties({"assignedTo", "createdBy"})
    private List<CafeTable> assignedTables = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"user"})
    private List<Product> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
//    @NotNull //commented to have possibility to set in controller
    @Column(length = 8, nullable = false)
    private UserRole role;

    @NotNull
    @Column(length = 63, nullable = false)
    private String name;

    @NotNull
    @Column(length = 63, nullable = false)
    private String password;

    @Email
    @NotEmpty
    @Column(unique=true)
    private String email;

    /*
    These lists are read-only to have a control over updates
     */
    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public List<CafeTable> getCreatedTables() {
//        return Collections.unmodifiableList(createdTables);
        return createdTables;
    }

    public void addTable(CafeTable table){
        table.setCreatedBy(this);
        createdTables.add(table);
    }

    public List<CafeTable> getAssignedTables() {
        return Collections.unmodifiableList(assignedTables);
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Allows to add a new product
     * @param product Product which should be added
     */
    public void addProduct(Product product){
        products.add(product);
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public boolean isManager(){
        return role.equals(UserRole.MANAGER);
    }
}
