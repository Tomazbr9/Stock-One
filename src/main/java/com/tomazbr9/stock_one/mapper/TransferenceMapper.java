package com.tomazbr9.stock_one.mapper;

import com.tomazbr9.stock_one.dto.TransfersResponse;
import com.tomazbr9.stock_one.entity.Transference;

import java.util.List;

public final class TransferenceMapper {

    public static List<TransfersResponse> toDtoList(List<Transference> transfers){
        return transfers.stream()
                .map(transfer -> new TransfersResponse(
                        transfer.getId(),
                        transfer.getDestinationUnit().getName(),
                        transfer.getRequester().getEmail(),
                        transfer.getStatus(),
                        transfer.getSubmissionDate(),
                        ItemMapper.toDtoList(transfer.getItems())
                )).toList();
    }
}
