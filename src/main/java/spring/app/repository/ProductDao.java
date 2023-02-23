package spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.domain.Product;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM public.product", nativeQuery = true)
    List<Product> getAllProducts(Pageable pageable);

    @Query(value = "SELECT * FROM public.product" +
            " WHERE id_product = ?1", nativeQuery = true)
    Optional<Product> getProductById(Long id);

    @Query(value = "SELECT * FROM public.product" +
            " WHERE nameProduct = ?1 AND cost = ?2", nativeQuery = true)
    Optional<Product> getProductByAllFields(String nameProduct, Double cost);
}
