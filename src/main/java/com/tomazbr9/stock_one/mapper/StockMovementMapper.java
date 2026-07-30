package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.RegisterStockMovementRequest;
import com.tomazbr9.stock_one.entity.StockMovement;
import com.tomazbr9.stock_one.entity.Unit;
import com.tomazbr9.stock_one.entity.User;

public final class StockMovementMapper {

    public static StockMovement toEntity(RegisterStockMovementRequest dto, Unit unit, User user){
        return StockMovement.builder()
                .unit(unit)
                .user(user)
                .typeMovement(dto.typeMovement())
                .transactionDate(dto.transactionDate())
                .invoice(dto.invoice())
                .observation(dto.observation())
                .build();
    }
}
