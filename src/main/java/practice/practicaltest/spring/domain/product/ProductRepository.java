package practice.practicaltest.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByProductSellingTypeIn(List<ProductSellingType> productSellingTypes);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

}
