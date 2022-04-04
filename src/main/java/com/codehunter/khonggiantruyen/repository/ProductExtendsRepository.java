package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.Product;
import com.codehunter.khonggiantruyen.domain.ProductWithLatestCommentDate;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ProductExtendsRepository extends ProductRepository {
    @Query(
        value = "select new com.codehunter.khonggiantruyen.domain.ProductWithLatestCommentDate(p, max(c.commentDate)) from Product p left join Comment c on p.id=c.product group by p.id order by max(c.commentDate) desc",
        countQuery = "select count(distinct p) from Product p"
    )
    Page<ProductWithLatestCommentDate> findAllNewsCommentedProduct(Pageable pageable);
}
