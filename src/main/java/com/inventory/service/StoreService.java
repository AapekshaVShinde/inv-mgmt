package com.inventory.service;

import com.inventory.dto.request.ProductSummary;
import com.inventory.dto.request.StoreRequest;
import com.inventory.dto.response.StoreResponse;
import com.inventory.entity.Product;
import com.inventory.entity.Store;
import com.inventory.exception.StoreException;
import com.inventory.repo.ProductRepository;
import com.inventory.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    /** Get all stores with pagination */
    public Page<StoreResponse> getAllStores(Pageable pageable) {
        return storeRepository.findAll(pageable).map(this::mapToResponse);
    }

    /** Get single store */
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new StoreException("STORE001", "Store not found", HttpStatus.NOT_FOUND));
        return mapToResponse(store);
    }

    /** Create store */
    public StoreResponse createStore(StoreRequest request) {
        if (storeRepository.existsByNameIgnoreCaseAndActiveTrue(request.getName().trim())) {
            throw new StoreException("STORE002", "Store with this name already exists", HttpStatus.BAD_REQUEST);
        }

        Store store = new Store();
        store.setName(request.getName().trim());
        store.setLocation(request.getLocation());
        store.setManagerName(request.getManagerName());
        store.setContactNumber(request.getContactNumber());
        store.setActive(true);

        return mapToResponse(storeRepository.save(store));
    }

    /** Update store */
    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new StoreException("STORE001", "Store not found", HttpStatus.NOT_FOUND));

        store.setName(request.getName().trim());
        store.setLocation(request.getLocation());
        store.setManagerName(request.getManagerName());
        store.setContactNumber(request.getContactNumber());

        return mapToResponse(storeRepository.save(store));
    }

    /** Soft delete */
    public void deleteStore(Long id) {
        Store store = storeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new StoreException("STORE001", "Store not found", HttpStatus.NOT_FOUND));
        store.setActive(false);
        storeRepository.save(store);
    }

    /** Add product to store */
    public StoreResponse addProductToStore(Long storeId, Long productId) {
        Store store = storeRepository.findByIdAndActiveTrue(storeId)
                .orElseThrow(() -> new StoreException("STORE001", "Store not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new StoreException("PROD001", "Product not found", HttpStatus.NOT_FOUND));

        product.setStore(store);
        productRepository.save(product);

        return mapToResponse(storeRepository.findById(storeId).get());
    }

    /** Remove product from store */
    public StoreResponse removeProductFromStore(Long storeId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new StoreException("PROD001", "Product not found", HttpStatus.NOT_FOUND));

        if (product.getStore() == null || !product.getStore().getId().equals(storeId)) {
            throw new StoreException("STORE003", "Product does not belong to this store", HttpStatus.BAD_REQUEST);
        }

        product.setStore(null);
        productRepository.save(product);

        Store store = storeRepository.findByIdAndActiveTrue(storeId)
                .orElseThrow(() -> new StoreException("STORE001", "Store not found", HttpStatus.NOT_FOUND));

        return mapToResponse(store);
    }

    /** Mapper */
    private StoreResponse mapToResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setName(store.getName());
        response.setLocation(store.getLocation());
        response.setManagerName(store.getManagerName());
        response.setContactNumber(store.getContactNumber());
        response.setActive(store.isActive());
        response.setProducts(store.getProducts() != null
                ? store.getProducts().stream().map(this::mapToProductSummary).collect(Collectors.toList())
                : List.of());
        return response;
    }

    private ProductSummary mapToProductSummary(Product product) {
        ProductSummary summary = new ProductSummary();
        summary.setId(product.getId());
        summary.setName(product.getName());
        summary.setQuantity(product.getQuantity());
        return summary;
    }
}

