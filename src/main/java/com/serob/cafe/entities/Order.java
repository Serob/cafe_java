package com.serob.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.serob.cafe.utils.OrderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private List<ProductInOrder> productsInOrder = new ArrayList<>();

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

    public List<ProductInOrder> getProductsInOrder() {
        return Collections.unmodifiableList(productsInOrder);
    }

    public void addProductsInOrder(ProductInOrder productInOrders) {
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
