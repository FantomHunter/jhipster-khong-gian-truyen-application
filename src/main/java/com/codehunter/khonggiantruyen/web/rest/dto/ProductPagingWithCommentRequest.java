package com.codehunter.khonggiantruyen.web.rest.dto;

import com.codehunter.khonggiantruyen.domain.Product;
import java.util.List;

public class ProductPagingWithCommentRequest {

    private final List<Product> products;
    private final Long totalElements;
    private final Integer totalPage;

    public ProductPagingWithCommentRequest(List<Product> products, Long totalElements, Integer totalPage) {
        this.products = products;
        this.totalElements = totalElements;
        this.totalPage = totalPage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
