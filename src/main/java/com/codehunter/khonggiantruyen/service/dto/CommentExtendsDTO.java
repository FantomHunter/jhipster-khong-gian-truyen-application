package com.codehunter.khonggiantruyen.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author codehunter
 */
public class CommentExtendsDTO {

    @NotBlank
    private String content;

    @NotNull
    private Long productId;

    public CommentExtendsDTO() {}

    public CommentExtendsDTO(String content, Long productId) {
        this.content = content;
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CommentExtendsDTO{" + "content='" + content + '\'' + ", productId=" + productId + '}';
    }
}
