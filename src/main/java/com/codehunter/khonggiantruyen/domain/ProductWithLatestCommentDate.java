package com.codehunter.khonggiantruyen.domain;

import java.time.Instant;

public class ProductWithLatestCommentDate {

    private Product product;
    private Instant commentDate;

    public ProductWithLatestCommentDate() {}

    public ProductWithLatestCommentDate(Product product, Instant commentDate) {
        this.product = product;
        this.commentDate = commentDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Instant getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Instant commentDate) {
        this.commentDate = commentDate;
    }
}
