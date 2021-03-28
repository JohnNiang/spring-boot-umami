package me.johnniang.umami.repository;

import me.johnniang.umami.entity.Account;
import me.johnniang.umami.entity.Website;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Website repository.
 *
 * @author johnniang
 */
public interface WebsiteRepository extends JpaRepository<Website, Integer> {

    Optional<Website> findByUuid(String uuid);

    Optional<Website> findByShareId(String shareId);

    List<Website> findAllByUser(Account user, Sort sort);

}
