package com.inventory.service;

import com.inventory.dto.request.ProductRequest;
import com.inventory.dto.response.ProductResponse;
import com.inventory.entity.Category;
import com.inventory.entity.Product;
import com.inventory.entity.Store;
import com.inventory.entity.Supplier;
import com.inventory.exception.ProductException;
import com.inventory.repo.CategoryRepository;
import com.inventory.repo.ProductRepository;
import com.inventory.repo.StoreRepository;
import com.inventory.repo.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final StoreRepository storeRepository;

    public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findByActiveTrue(PageRequest.of(page, size));
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("PROD001", "Product not found", HttpStatus.NOT_FOUND));
    }

    public ProductResponse createProduct(String name, ProductRequest productRequest) {

        if (productRepository.existsByNameIgnoreCaseAndActiveTrue(name.trim())) {
            throw new ProductException("PROD002", "Product with this name already exists", HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ProductException("PROD003", "Category not found", HttpStatus.NOT_FOUND));

        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId())
                .orElseThrow(() -> new ProductException("PROD004", "Supplier not found", HttpStatus.NOT_FOUND));

        Store store = storeRepository.findById(productRequest.getStoreId())
                .orElseThrow(() -> new ProductException("PROD005", "Store not found", HttpStatus.NOT_FOUND));

        Product product = new Product();
        product.setName(name.trim());
        product.setDescription(product.getDescription());
        product.setPrice(product.getPrice());
        product.setQuantity(product.getQuantity());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setStore(store);
        product.setActive(true);

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {

        Product product = getProductById(id);

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ProductException("PROD003", "Category not found", HttpStatus.NOT_FOUND));

        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId())
                .orElseThrow(() -> new ProductException("PROD004", "Supplier not found", HttpStatus.NOT_FOUND));

        Store store = storeRepository.findById(productRequest.getStoreId())
                .orElseThrow(() -> new ProductException("PROD005", "Store not found", HttpStatus.NOT_FOUND));

        product.setName(productRequest.getName().trim());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setStore(store);

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false); // Soft delete
        productRepository.save(product);
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setCategoryName(product.getCategory().getName());
        response.setSupplierName(product.getSupplier().getName());
        response.setStoreName(product.getStore().getName());
        return response;
    }
}
