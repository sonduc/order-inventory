package com.example.orderinventory.application.product;

import com.example.orderinventory.application.product.dto.CreateProductRequest;
import com.example.orderinventory.application.product.dto.ProductResponse;
import com.example.orderinventory.application.product.dto.StockAdjustmentRequest;
import com.example.orderinventory.application.product.dto.UpdateProductRequest;
import com.example.orderinventory.application.product.mapper.ProductMapper;
import com.example.orderinventory.domain.product.ProductService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductApplicationService {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductApplicationService(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> listProducts() {
        return productService.listProducts()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long productId) {
        return productMapper.toResponse(productService.getRequiredProduct(productId));
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        return productMapper.toResponse(
                productService.createProduct(request.name(), request.description(), request.price(), request.stockQuantity())
        );
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        return productMapper.toResponse(
                productService.updateProduct(productId, request.name(), request.description(), request.price(), request.stockQuantity())
        );
    }

    @Transactional
    public ProductResponse adjustStock(Long productId, StockAdjustmentRequest request) {
        return productMapper.toResponse(
                productService.adjustStock(
                        productId,
                        request.quantityDelta(),
                        request.decrease(),
                        request.expectedVersion(),
                        request.lockMode(),
                        request.reason()
                )
        );
    }
}
