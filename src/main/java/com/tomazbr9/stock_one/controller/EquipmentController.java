package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.CreateEquipmentRequest;
import com.tomazbr9.stock_one.security.UserDetailsImpl;
import com.tomazbr9.stock_one.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService service;

    @PostMapping
    public ResponseEntity<UUID> createEquipment(
            @RequestBody CreateEquipmentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UUID equipmentId = service.createEquipment(request, userDetails.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(equipmentId)
                .toUri();

        return ResponseEntity.created(location).body(equipmentId);
    }
}
