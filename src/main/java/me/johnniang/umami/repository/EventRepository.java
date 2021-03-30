package me.johnniang.umami.repository;

import me.johnniang.umami.entity.Event;
import me.johnniang.umami.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Event repository.
 *
 * @author johnniang
 */
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findAllByWebsiteInAndCreatedAtGreaterThanEqual(Collection<Website> websites, LocalDateTime startAt);

}
