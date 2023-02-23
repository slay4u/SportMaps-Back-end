package spring.app.service;

import spring.app.dto.ProductAllInfoDto;
import spring.app.dto.ProductCreateDto;
import spring.app.dto.ProductInfoDto;

import java.util.List;

public interface ProductService {
    ProductInfoDto createProduct(ProductCreateDto product);

    ProductInfoDto updateProduct(Long id, ProductCreateDto product);

    ProductAllInfoDto getProductById(Long id);

    void deleteById(Long id);

    List<ProductAllInfoDto> getAllProducts(int pageNumber);
}
