package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.Comment;
import com.codehunter.khonggiantruyen.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author codehunter
 */
@Repository
public interface CommentExtendsRepository extends CommentRepository {
    @Query(
        value = "select distinct comment from Comment comment where product.id =:id",
        countQuery = "select count(distinct comment) from Comment comment"
    )
    Page<Comment> findAllWithEagerRelationships(Pageable pageable, @Param("id") Long id);
}
