package com.serob.cafe.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
public class Product extends BaseEntity{

    @ManyToOne()
//    @Fetch(value = FetchMode.SUBSELECT)
//    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product")
    private Set<ProductInOrder> productInOrders;

    @Column(length = 63)
    private String name;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ProductInOrder> getProductInOrders() {
        return Collections.unmodifiableSet(productInOrders);
    }

    public void addProductInOrders(ProductInOrder productInOrders) {
        productInOrders.setProduct(this);
        this.productInOrders.add(productInOrders);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
