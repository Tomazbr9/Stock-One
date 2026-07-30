package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.CreateUnitRequest;
import com.tomazbr9.stock_one.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

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
}
