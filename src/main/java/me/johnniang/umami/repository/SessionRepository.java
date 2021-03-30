package me.johnniang.umami.repository;

import me.johnniang.umami.entity.Session;
import me.johnniang.umami.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Session repository.
 *
 * @author johnniang
 */
public interface SessionRepository extends JpaRepository<Session, Integer> {

    Optional<Session> findByUuid(String uuid);

    List<Session> findAllByWebsiteInAndCreatedAtGreaterThanEqual(Collection<Website> websites, LocalDateTime startAt);

}
