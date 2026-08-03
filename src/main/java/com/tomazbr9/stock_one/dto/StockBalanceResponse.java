package com.tomazbr9.stock_one.dto;

import java.util.UUID;

public record StockBalanceResponse(
        UUID productId,
        String productName,
        String categoryName,
        Integer currentQuantity,
        Integer minimumStock
) {}