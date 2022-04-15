package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.Product;
import com.codehunter.khonggiantruyen.domain.ProductWithLatestCommentDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductExtendsRepository extends ProductRepository {
    @Query(
        value = "select new com.codehunter.khonggiantruyen.domain.ProductWithLatestCommentDate(p, max(c.commentDate)) from Product p inner join Comment c on p.id=c.product group by p.id order by max(c.commentDate) desc",
        countQuery = "select count(distinct p) from Product p"
    )
    Page<ProductWithLatestCommentDate> findAllNewsCommentedProduct(Pageable pageable);

    @Query(value = "select product.id from Product product", countQuery = "select count(product.id) from Product product")
    Page<Long> getProductIds(Pageable pageable);

    @Query(
        value = "select distinct product from Product product left join fetch product.comments where product.id in (:listProducts)",
        countQuery = "select count(distinct product) from Product product"
    )
    List<Product> findAllProducts(@Param("listProducts") List<Long> listProducts, Sort sort);
}
