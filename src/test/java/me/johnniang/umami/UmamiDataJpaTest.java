package me.johnniang.umami;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring data jpa loading test.
 *
 * @author johnniang
 */
@DataJpaTest
@ActiveProfiles("test")
class UmamiDataJpaTest {

    @Test
    void contextLoad() {

    }
}
