package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.TrolleyItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class TrolleyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TrolleyRepository trolleyRepository;

    @Test
    public void whenFindByItem_thenReturnTroelleyItem() {
        // given
        TrolleyItem item = new TrolleyItem("apple", 1);
        entityManager.persist(item);
        entityManager.flush();

        // when
        TrolleyItem found = trolleyRepository.getOne(item.getItem());

        // then
        assertThat(((TrolleyItem) found).getItem())
                .isEqualTo(item.getItem());
    }
}
