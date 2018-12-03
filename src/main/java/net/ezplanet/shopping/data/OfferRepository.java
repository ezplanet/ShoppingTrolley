package net.ezplanet.shopping.data;

import net.ezplanet.shopping.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface OfferRepository extends JpaRepository<Offer, String> {

    @Override
    public Offer save(Offer offer);

    @Override
    public List<Offer> findAll();

    @Override
    public Offer getOne(String code);

}
