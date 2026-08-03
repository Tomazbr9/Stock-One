package com.tomazbr9.stock_one.dto;

import java.util.UUID;

public record ItemTransferResponse(
        UUID id,
        String itemName,
        Integer quantity
) {
}
