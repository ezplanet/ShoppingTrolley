package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByName_thenReturnProduct() {
        // given
        Product apple = new Product("apple", "A11", BigDecimal.valueOf(0.60));
        entityManager.persist(apple);
        entityManager.flush();

        // when
        Product found = productRepository.getOne(apple.getName());

        // then
        assertThat(found.getName())
                .isEqualTo(apple.getName());
    }
}
