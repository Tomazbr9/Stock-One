package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.StockBalanceResponse;
import com.tomazbr9.stock_one.entity.StockUnit;
import com.tomazbr9.stock_one.entity.User;
import com.tomazbr9.stock_one.exception.BusinessRuleException;
import com.tomazbr9.stock_one.exception.ResourceNotFoundException;
import com.tomazbr9.stock_one.repository.StockUnitRepository;
import com.tomazbr9.stock_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockUnitService {

    private final StockUnitRepository stockUnitRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<StockBalanceResponse> getConsumableStockByUnit(UUID userId){

        return stockUnitRepository.findByUnitId(userId).stream()
                .map(stock -> new StockBalanceResponse(
                        stock.getProduct().getId(),
                        stock.getProduct().getName(),
                        stock.getProduct().getCategory().getName(),
                        stock.getCurrentQuantity(),
                        stock.getMinimumStock()
                )).toList();
    }
}
