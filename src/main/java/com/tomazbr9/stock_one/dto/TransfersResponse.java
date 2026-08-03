package com.tomazbr9.stock_one.dto;

import com.tomazbr9.stock_one.entity.TransferItem;
import com.tomazbr9.stock_one.enums.TransferStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TransfersResponse(
        UUID id,
        String destinationUnitName,
        String username,
        TransferStatus status,
        LocalDate submissionDate,
        List<ItemTransferResponse> items
) {
}
