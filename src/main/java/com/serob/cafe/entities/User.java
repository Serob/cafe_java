package com.serob.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serob.cafe.utils.UserRole;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class User extends BaseEntity{

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Order> orders;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties({"assignedTo", "createdBy",})
    private Set<CafeTable> createdTables;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnoreProperties({"assignedTo", "createdBy"})
    private Set<CafeTable> assignedTables;

    @OneToMany(mappedBy = "user")
//    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"user"})
    private Set<Product> products;

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
    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

    public Set<CafeTable> getCreatedTables() {
//        return Collections.unmodifiableList(createdTables);
        return Collections.unmodifiableSet(createdTables);
    }

    public void addTable(CafeTable table){
        table.setCreatedBy(this);
        createdTables.add(table);
    }

    public Set<CafeTable> getAssignedTables() {
        return Collections.unmodifiableSet(assignedTables);
    }

    public Set<Product> getProducts() {
        return products;
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
