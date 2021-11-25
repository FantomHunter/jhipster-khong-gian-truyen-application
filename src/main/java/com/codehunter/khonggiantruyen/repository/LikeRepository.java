package com.codehunter.khonggiantruyen.repository;

import com.codehunter.khonggiantruyen.domain.Like;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Like entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("select jhiLike from Like jhiLike where jhiLike.user.login = ?#{principal.username}")
    List<Like> findByUserIsCurrentUser();
}
