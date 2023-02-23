package spring.app.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.exception.AlreadyExistException;
import spring.app.exception.NotFoundException;
import spring.app.domain.Product;
import spring.app.dto.ProductAllInfoDto;
import spring.app.dto.ProductCreateDto;
import spring.app.dto.ProductInfoDto;
import spring.app.repository.ProductDao;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final int PAGE_ELEMENTS_AMOUNT = 15;

    private final ProductDao productDao;

    @Override
    public ProductInfoDto createProduct(ProductCreateDto productDto) {
        validateProduct(productDto);
        Product product = convertToEntity(productDto, new Product());
        productDao.save(product);
        return convertEntityToDto(product, "created");
    }

    @Override
    public ProductInfoDto updateProduct(Long id, ProductCreateDto productDto) {
        validateProduct(productDto);
        Product product = convertToEntity(productDto, new Product());
        Product savedProduct = productDao.save(updateContent(product, getById(id)));
        return convertEntityToDto(savedProduct, "updated");
    }

    @Override
    public ProductAllInfoDto getProductById(Long id) {
        Product byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        productDao.deleteById(id);
    }

    @Override
    public List<ProductAllInfoDto> getAllProducts(int pageNumber) {
        if(pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<Product> allProducts = productDao.getAllProducts(
                (Pageable) PageRequest.of(pageNumber,
                        PAGE_ELEMENTS_AMOUNT)
        );
        return listToDto(allProducts);
    }

    private void validateProduct(ProductCreateDto product) {
        validatePresentProduct(product);
        if (product.getNameProduct().isBlank() || Objects.isNull(product.getNameProduct())) {
            throw new IllegalArgumentException("Product's name is not valid");
        }
        if (product.getCost().isNaN() || Objects.isNull(product.getCost())) {
            throw new IllegalArgumentException("Product's cost is not valid");
        }
    }

    private void validatePresentProduct(ProductCreateDto productDto) {
        String nameProduct = productDto.getNameProduct();
        Double cost = productDto.getCost();
        Optional<Product> result = productDao
                .getProductByAllFields(nameProduct, cost);
        if(result.isPresent()) {
            String body = """
          {
              "message": "The product already exists!",
              "nameProduct": "%s",
              "cost": "%s"
          }
          """.formatted(nameProduct, cost);
            throw new AlreadyExistException(body);
        }
    }

    private Product updateContent(Product product, Product resultProduct) {
        resultProduct.setNameProduct(product.getNameProduct());
        resultProduct.setCost(product.getCost());
        return resultProduct;
    }

    private Product convertToEntity(ProductCreateDto productDto, Product product) {
        product.setNameProduct(productDto.getNameProduct());
        product.setCost(productDto.getCost());
        return product;
    }

    private ProductInfoDto convertEntityToDto(Product product, String state) {
        return ProductInfoDto.builder()
                .id(product.getIdProduct())
                .result("Product "
                        + product.getNameProduct()
                        + " " + product.getCost() +
                        " " + state + " successfully!")
                .build();
    }

    private Product getById(Long id) {
        Optional<Product> resultProduct = productDao.getProductById(id);
        if(resultProduct.isEmpty()) {
            throw new NotFoundException("Product by id is not found!");
        }
        return resultProduct.get();
    }

    private ProductAllInfoDto allInfoDto(Product product) {
        return new ProductAllInfoDto(
                product.getNameProduct(),
                product.getCost()
        );
    }

    private List<ProductAllInfoDto> listToDto(List<Product> products) {
        return products.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
