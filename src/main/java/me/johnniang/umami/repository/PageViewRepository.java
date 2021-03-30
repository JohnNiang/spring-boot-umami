package me.johnniang.umami.repository;

import me.johnniang.umami.entity.PageView;
import me.johnniang.umami.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Page view repository.
 *
 * @author johnniang
 */
public interface PageViewRepository extends JpaRepository<PageView, Integer> {

    List<PageView> findAllByWebsiteInAndCreatedAtGreaterThanEqual(Collection<Website> websites, LocalDateTime startAt);

}
