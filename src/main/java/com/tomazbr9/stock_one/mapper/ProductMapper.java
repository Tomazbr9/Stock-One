package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.CreateProductRequest;
import com.tomazbr9.stock_one.entity.Category;
import com.tomazbr9.stock_one.entity.Product;

public final class ProductMapper {

    public static Product toEntity(CreateProductRequest request, Category category){
        return Product.builder()
                .code(request.code())
                .name(request.name())
                .category(category)
                .unitMeasurement(request.unitMeasurement())
                .build();
    }
}
