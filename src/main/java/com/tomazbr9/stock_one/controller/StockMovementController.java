package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.RegisterStockMovementRequest;
import com.tomazbr9.stock_one.security.UserDetailsImpl;
import com.tomazbr9.stock_one.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService service;

    @PostMapping
    public ResponseEntity<UUID> registerStockMovement(@RequestBody RegisterStockMovementRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        UUID response = service.registerStockMovement(request, userDetails.getId());

        return ResponseEntity.created(URI.create("api/v1/stock-movements/" + response)).body(response);
    }

}
