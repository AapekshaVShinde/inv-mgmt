package com.inventory.service;

import com.inventory.dto.request.SupplierRequest;
import com.inventory.dto.response.SupplierResponse;
import com.inventory.entity.Supplier;
import com.inventory.exception.SupplierException;
import com.inventory.repo.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Page<SupplierResponse> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable).map(this::mapToResponse);
    }

    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new SupplierException("SUP001", "Supplier not found", HttpStatus.NOT_FOUND));
        return mapToResponse(supplier);
    }

    public SupplierResponse createSupplier(SupplierRequest request) {
        if (supplierRepository.existsByNameIgnoreCaseAndActiveTrue(request.getName().trim())) {
            throw new SupplierException("SUP002", "Supplier with this name already exists", HttpStatus.BAD_REQUEST);
        }

        Supplier supplier = new Supplier();
        supplier.setName(request.getName().trim());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setActive(true);

        return mapToResponse(supplierRepository.save(supplier));
    }

    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new SupplierException("SUP001", "Supplier not found", HttpStatus.NOT_FOUND));

        supplier.setName(request.getName().trim());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());

        return mapToResponse(supplierRepository.save(supplier));
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new SupplierException("SUP001", "Supplier not found", HttpStatus.NOT_FOUND));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }

    private SupplierResponse mapToResponse(Supplier supplier) {
        SupplierResponse response = new SupplierResponse();
        response.setId(supplier.getId());
        response.setName(supplier.getName());
        response.setContactPerson(supplier.getContactPerson());
        response.setPhone(supplier.getPhone());
        response.setEmail(supplier.getEmail());
        response.setAddress(supplier.getAddress());
        response.setActive(supplier.isActive());
        return response;
    }
}
