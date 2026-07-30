package com.tomazbr9.stock_one.controller;

import com.tomazbr9.stock_one.dto.SolicitTransferenceRequest;
import com.tomazbr9.stock_one.dto.TransfersResponse;
import com.tomazbr9.stock_one.security.UserDetailsImpl;
import com.tomazbr9.stock_one.service.TransferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transferences")
@RequiredArgsConstructor
public class TransferenceController {

    private final TransferenceService service;

    @GetMapping("/pending")
    public ResponseEntity<?> getTransfersPending(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        List<TransfersResponse> response = service.getTransfersPending(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/solicit")
    public ResponseEntity<UUID> solicitTransfer(
            @RequestBody SolicitTransferenceRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UUID response = service.solicitTransfer(request, userDetails.getId());

        return ResponseEntity.created(URI.create("api/v1/transferences/" + response)).body(response);

    }

    @PatchMapping("/{transferId}/transfer")
    public ResponseEntity<UUID> transfer(
            @PathVariable UUID transferId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UUID response = service.transfer(transferId, userDetails.getId());
        return ResponseEntity.accepted().body(response);
    }
}
