package com.serob.cafe.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Product extends BaseEntity{

    @ManyToOne()
    private User user;

    @OneToMany(mappedBy = "product")
    private List<ProductInOrder> productInOrders = new ArrayList<>();

    @Column(length = 63)
    private String name;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ProductInOrder> getProductInOrders() {
        return Collections.unmodifiableList(productInOrders);
    }

    public void addProductInOrders(ProductInOrder productInOrders) {
        this.productInOrders.add(productInOrders);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
