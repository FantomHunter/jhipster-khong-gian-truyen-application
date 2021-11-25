package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.ResourceDownload;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResourceDownload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceDownloadRepository extends JpaRepository<ResourceDownload, Long> {}
