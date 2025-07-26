package com.inventory.controller;

import com.inventory.constants.CommonConstants;
import com.inventory.dto.request.StoreRequest;
import com.inventory.dto.response.StoreResponse;
import com.inventory.service.StoreService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CommonConstants.ApiPaths.BASE_API_PATH_WITH_VERSION + "/stores")
@RequiredArgsConstructor
//@Hidden
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Page<StoreResponse>> getAllStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.getAllStores(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@RequestBody StoreRequest request) {
        return ResponseEntity.ok(storeService.createStore(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable Long id, @RequestBody StoreRequest request) {
        return ResponseEntity.ok(storeService.updateStore(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    /** Add product to store */
    @PostMapping("/{storeId}/products/{productId}")
    public ResponseEntity<StoreResponse> addProductToStore(@PathVariable Long storeId, @PathVariable Long productId) {
        return ResponseEntity.ok(storeService.addProductToStore(storeId, productId));
    }

    /** Remove product from store */
    @DeleteMapping("/{storeId}/products/{productId}")
    public ResponseEntity<StoreResponse> removeProductFromStore(@PathVariable Long storeId, @PathVariable Long productId) {
        return ResponseEntity.ok(storeService.removeProductFromStore(storeId, productId));
    }
}
