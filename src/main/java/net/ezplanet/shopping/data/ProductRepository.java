package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {

    @Override
    public Product save (Product product);

    @Override
    public List<Product> findAll();

    @Override
    public Product getOne (String name);

}
