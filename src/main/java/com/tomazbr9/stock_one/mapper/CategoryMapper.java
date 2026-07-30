package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.CreateCategoryRequest;
import com.tomazbr9.stock_one.entity.Category;

public final class CategoryMapper {

    public static Category toEntity(CreateCategoryRequest dto){
        return Category.builder()
                .name(dto.name())
                .build();
    }
}
