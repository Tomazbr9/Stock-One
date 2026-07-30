package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.ItemMovementRequest;
import com.tomazbr9.stock_one.dto.RegisterStockMovementRequest;
import com.tomazbr9.stock_one.entity.*;
import com.tomazbr9.stock_one.exception.ResourceNotFoundException;
import com.tomazbr9.stock_one.exception.BusinessRuleException;
import com.tomazbr9.stock_one.mapper.StockMovementMapper;
import com.tomazbr9.stock_one.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StockUnitRepository stockUnitRepository;

    @Transactional
    public UUID registerStockMovement(RegisterStockMovementRequest request, UUID userId) {
        log.info("Iniciando registro de movimentação do tipo {} para a unidade {}", request.typeMovement(), request.unitId());

        Unit unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada."));

        User user = userRepository.getReferenceById(userId);

        List<UUID> productIds = request.items().stream()
                .map(ItemMovementRequest::productId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            log.error("Tentativa de movimentação com produtos inexistentes.");
            throw new ResourceNotFoundException("Um ou mais produtos informados são inválidos ou não existem.");
        }

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));


        StockMovement movement = StockMovementMapper.toEntity(request, unit, user);

        List<ItemMovement> items = request.items().stream()
                .map(dto -> ItemMovement.builder()
                        .stockMovement(movement)
                        .product(productMap.get(dto.productId()))
                        .quantity(dto.quantity())
                        .build())
                .toList();

        movement.setItems(items);

        StockMovement savedMovement = stockMovementRepository.save(movement);

        // Atualização do Saldo de Estoque (StockUnit)
        List<StockUnit> stockUnits = stockUnitRepository
                .findByUnitIdAndProductIdIn(unit.getId(), productIds);

        Map<UUID, StockUnit> stockUnitMap = stockUnits.stream()
                .collect(Collectors.toMap(
                        su -> su.getProduct().getId(),
                        su -> su
                ));

        List<StockUnit> stockUnitsToSave = new ArrayList<>();

        for (ItemMovement item : savedMovement.getItems()) {

            StockUnit stockUnit = stockUnitMap.computeIfAbsent(
                    item.getProduct().getId(),
                    id -> StockUnit.builder()
                            .unit(unit)
                            .product(item.getProduct())
                            .currentQuantity(0)
                            .minimumStock(5)
                            .build()
            );

            switch (request.typeMovement()) {
                case ENTRY -> stockUnit.setCurrentQuantity(
                        stockUnit.getCurrentQuantity() + item.getQuantity()
                );
                case EXIT -> {
                    if (stockUnit.getCurrentQuantity() < item.getQuantity()) {
                        log.warn("Estoque insuficiente. Produto: {}, Estoque atual: {}, Solicitado: {}",
                                item.getProduct().getName(), stockUnit.getCurrentQuantity(), item.getQuantity());
                        throw new BusinessRuleException(
                                "Estoque insuficiente para o produto: " + item.getProduct().getName()
                        );
                    }
                    stockUnit.setCurrentQuantity(
                            stockUnit.getCurrentQuantity() - item.getQuantity()
                    );
                }
                default -> throw new BusinessRuleException("Tipo de movimentação inválido.");
            }

            stockUnitsToSave.add(stockUnit);
        }

        stockUnitRepository.saveAll(stockUnitsToSave);

        log.info("Movimentação registrada com sucesso. ID: {}", savedMovement.getId());

        return savedMovement.getId();
    }
}