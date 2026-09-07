package com.example.orderinventory.presentation.rest;

import com.example.orderinventory.application.product.ProductApplicationService;
import com.example.orderinventory.application.product.dto.CreateProductRequest;
import com.example.orderinventory.application.product.dto.ProductResponse;
import com.example.orderinventory.application.product.dto.StockAdjustmentRequest;
import com.example.orderinventory.application.product.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @GetMapping("/api/products")
    public List<ProductResponse> listProducts() {
        return productApplicationService.listProducts();
    }

    @GetMapping("/api/products/{productId}")
    public ProductResponse getProduct(@PathVariable Long productId) {
        return productApplicationService.getProduct(productId);
    }

    @PostMapping("/api/admin/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productApplicationService.createProduct(request);
    }

    @PutMapping("/api/admin/products/{productId}")
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public ProductResponse updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return productApplicationService.updateProduct(productId, request);
    }

    @PatchMapping("/api/admin/products/{productId}/stock")
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    public ProductResponse adjustStock(
            @PathVariable Long productId,
            @Valid @RequestBody StockAdjustmentRequest request
    ) {
        return productApplicationService.adjustStock(productId, request);
    }
}
