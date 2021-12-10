package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.ResourceDownload;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResourceDownload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceDownloadRepository extends JpaRepository<ResourceDownload, Long> {
    @Query("select resourceDownload from ResourceDownload resourceDownload where product.id = :productId")
    List<ResourceDownload> findResourceDownloadByProduct(@Param("productId") Long id);
}
