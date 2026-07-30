package com.tomazbr9.stock_one.dto;

import java.util.UUID;

public record TransferItemRequest(
        UUID productId,
        UUID equipmentId,
        Integer quantity
) {
}
