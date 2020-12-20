package com.serob.cafe.entities;

import com.serob.cafe.utils.ProductStatus;

import javax.persistence.*;

@Entity
public class ProductInOrder extends BaseEntity{

    @ManyToOne()
    private Order order;

    @ManyToOne()
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private ProductStatus status;

    private int amount;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
