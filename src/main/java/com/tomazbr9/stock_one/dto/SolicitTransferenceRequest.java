package com.tomazbr9.stock_one.dto;

import java.util.List;
import java.util.UUID;

public record SolicitTransferenceRequest(
    UUID sourceUnit,
    List<TransferItemRequest> items
){}
