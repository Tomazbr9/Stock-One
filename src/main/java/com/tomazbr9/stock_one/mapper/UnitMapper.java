package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.CreateUnitRequest;
import com.tomazbr9.stock_one.entity.Unit;

public final class UnitMapper {

    public static Unit toEntity(CreateUnitRequest dto){
        return Unit.builder()
                .name(dto.name())
                .isCentral(dto.isCentral())
                .build();
    }
}
