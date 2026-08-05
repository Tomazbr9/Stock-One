package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.CreateUnitRequest;
import com.tomazbr9.stock_one.dto.StockBalanceResponse;
import com.tomazbr9.stock_one.service.StockMovementService;
import com.tomazbr9.stock_one.service.StockUnitService;
import com.tomazbr9.stock_one.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;
    private final StockUnitService stockUnitService;

    @PostMapping
    public ResponseEntity<UUID> createUnit(@RequestBody @Valid CreateUnitRequest request){

        UUID unitId = unitService.createUnit(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(unitId)
                .toUri();

        return ResponseEntity.created(location).body(unitId);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<StockBalanceResponse>> getStockByUnit(@PathVariable UUID id){
        List<StockBalanceResponse> response = stockUnitService.getConsumableStockByUnit(id);
        return ResponseEntity.ok(response);
    }
}
