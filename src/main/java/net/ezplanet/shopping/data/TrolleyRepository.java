package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.TrolleyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TrolleyRepository extends JpaRepository<TrolleyItem, String> {

    @Override
    public TrolleyItem save(TrolleyItem trolleyItem);

    @Override
    public List<TrolleyItem> findAll();

    @Override
    public TrolleyItem getOne(String item);

    @Query(value = "SELECT * FROM trolley_item ORDER by offer, price ASC",
            nativeQuery = true)
    List<TrolleyItem> findAllOrderByOfferPriceAsc();
}
