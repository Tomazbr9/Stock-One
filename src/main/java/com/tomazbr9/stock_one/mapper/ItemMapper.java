package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.ItemTransferResponse;
import com.tomazbr9.stock_one.entity.TransferItem;

import java.util.List;

public final class ItemMapper {

    public static List<ItemTransferResponse> toDtoList(List<TransferItem> items){
        return items.stream()
                .map(item -> {

                    String itemName = item.getProduct() != null
                            ? item.getProduct().getName() : item.getEquipment().getName();

                    return new ItemTransferResponse(
                            item.getId(),
                            itemName,
                            item.getQuantity()
                    );
                }).toList();
    }
}
