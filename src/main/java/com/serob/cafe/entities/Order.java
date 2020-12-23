package com.serob.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serob.cafe.utils.OrderStatus;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="order_item")
public class Order extends BaseEntity{

    @ManyToOne()
    @JsonIgnoreProperties({"orders"})
    private User user;

    @ManyToOne()
    @JoinColumn(name = "table_id")
    private CafeTable table;

    @OneToMany(mappedBy = "order")
    private Set<ProductInOrder> productsInOrder = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private OrderStatus status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CafeTable getTable() {
        return table;
    }

    public void setTable(CafeTable table) {
        this.table = table;
    }

    public Set<ProductInOrder> getProductsInOrder() {
        return Collections.unmodifiableSet(productsInOrder);
    }

    public void addProductsInOrder(ProductInOrder productInOrders) {
        productInOrders.setOrder(this);
        this.productsInOrder.add(productInOrders);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public boolean isOpen(){
        return status == OrderStatus.OPEN;
    }
}
