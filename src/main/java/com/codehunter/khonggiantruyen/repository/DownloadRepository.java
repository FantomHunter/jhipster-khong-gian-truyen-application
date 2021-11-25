package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.Download;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Download entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DownloadRepository extends JpaRepository<Download, Long> {
    @Query("select download from Download download where download.user.login = ?#{principal.username}")
    List<Download> findByUserIsCurrentUser();
}
