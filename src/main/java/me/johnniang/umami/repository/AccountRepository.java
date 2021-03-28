package me.johnniang.umami.repository;

import me.johnniang.umami.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Account repository.
 *
 * @author johnniang
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    boolean existsByUsername(String username);

    Optional<Account> findByUsername(String username);
}
