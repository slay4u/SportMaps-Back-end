package spring.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.app.dto.ProductAllInfoDto;
import spring.app.dto.ProductCreateDto;
import spring.app.dto.ProductInfoDto;
import spring.app.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductInfoDto createProduct(@RequestBody ProductCreateDto requestToSave) {
        return productService.createProduct(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(HttpStatus.OK)
    public List<ProductAllInfoDto> getAllProducts(int page_num) {
        return productService.getAllProducts(page_num);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductAllInfoDto getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductInfoDto updateProductById(@PathVariable("id") Long id,
                                          @RequestBody ProductCreateDto requestToSave) {
        return productService.updateProduct(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductById(@PathVariable("id") Long id) {
        productService.deleteById(id);
    }
}
