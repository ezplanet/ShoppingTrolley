/*
 * (C) Copyright ${year} Mauro Mozzarelli.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ...
 */
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
