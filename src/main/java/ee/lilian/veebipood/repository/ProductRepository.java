package ee.lilian.veebipood.repository;
import ee.lilian.veebipood.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//repository on andmebaasiga suhtlemiseks, lisamiseks, muutmiseks, kustutamiseks jne
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findAllByCategoryId(Pageable pageable, Long categoryId);
}

