package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.ItemTransferResponse;
import com.tomazbr9.stock_one.dto.SolicitTransferenceRequest;
import com.tomazbr9.stock_one.dto.TransferItemRequest;
import com.tomazbr9.stock_one.dto.TransfersResponse;
import com.tomazbr9.stock_one.entity.*;
import com.tomazbr9.stock_one.enums.TransferStatus;
import com.tomazbr9.stock_one.exception.UnitNotFoundException;
import com.tomazbr9.stock_one.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransferenceService {

    private final UserRepository userRepository;
    private final UnitRepository unitRepository;
    private final ProductRepository productRepository;
    private final EquipmentRepository equipmentRepository;
    private final TransferenceRepository transferenceRepository;

    @Transactional
    public UUID solicitTransfer(SolicitTransferenceRequest request, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Unit destinationUnit = user.getUnit();
        if (destinationUnit == null) {
            throw new RuntimeException("Usuário não está vinculado a nenhuma unidade");
        }

        Unit sourceUnit = unitRepository.findById(request.sourceUnit())
                .orElseThrow(() -> new UnitNotFoundException("Unidade não encontrada"));

        if (sourceUnit.getId().equals(destinationUnit.getId())) {
            throw new RuntimeException("Unidade de origem não pode ser igual a unidade de destino");
        }

        Transference transference = Transference.builder()
                .sourceUnit(sourceUnit)
                .destinationUnit(destinationUnit)
                .requester(user)
                .status(TransferStatus.REQUESTED)
                .submissionDate(LocalDate.now())
                .build();

        // 1. Coleta apenas os IDs não nulos para busca em lote (Evita passar null para o banco)
        List<UUID> productIds = request.items().stream()
                .map(TransferItemRequest::productId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        List<UUID> equipmentIds = request.items().stream()
                .map(TransferItemRequest::equipmentId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 2. Busca em lote no banco de dados
        List<Product> products = productIds.isEmpty() ? Collections.emptyList() : productRepository.findAllById(productIds);
        List<Equipment> equipments = equipmentIds.isEmpty() ? Collections.emptyList() : equipmentRepository.findAllById(equipmentIds);

        // Valida se todos os produtos encontrados correspondem aos enviados
        if (products.size() != productIds.size()) {
            throw new RuntimeException("Um ou mais produtos informados são inválidos ou não existem.");
        }
        // Valida se todos os equipamentos encontrados correspondem aos enviados
        if (equipments.size() != equipmentIds.size()) {
            throw new RuntimeException("Um ou mais equipamentos informados são inválidos ou não existem.");
        }

        // 3. Cria mapas para lookup em O(1)
        Map<UUID, Product> productsMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<UUID, Equipment> equipmentMap = equipments.stream()
                .collect(Collectors.toMap(Equipment::getId, e -> e));

        // 4. Processa item por item da requisição (Permitindo itens mistos ou isolados com segurança)
        List<TransferItem> transferItems = new ArrayList<>();

        for (TransferItemRequest itemDto : request.items()) {
            boolean hasProduct = itemDto.productId() != null;
            boolean hasEquipment = itemDto.equipmentId() != null;

            // Validação item a item de exclusividade mútua
            if ((hasProduct && hasEquipment) || (!hasProduct && !hasEquipment)) {
                throw new RuntimeException("Cada item de transferência deve referenciar exatamente um Produto ou Equipamento, nunca ambos ou nenhum");
            }

            TransferItem.TransferItemBuilder itemBuilder = TransferItem.builder()
                    .transference(transference)
                    .quantity(itemDto.quantity());

            if (hasProduct) {
                itemBuilder.product(productsMap.get(itemDto.productId()));
            } else {
                itemBuilder.equipment(equipmentMap.get(itemDto.equipmentId()));
            }

            transferItems.add(itemBuilder.build());
        }

        transference.setItems(transferItems);

        Transference savedTransference = transferenceRepository.save(transference);

        return savedTransference.getId();
    }

    public UUID transfer(UUID transferId, UUID userId){

        User user = userRepository.getReferenceById(userId);

        Transference transference = transferenceRepository.findById(transferId).orElseThrow(() -> new RuntimeException("Transferencia não encontrada"));

        transference.setStatus(TransferStatus.IN_TRANSIT);
        transference.setReceiver(user);

        Transference savedTransference = transferenceRepository.save(transference);

        return savedTransference.getId();
    }

    public List<TransfersResponse> getTransfersPending(UUID userId){

        User user = userRepository.getReferenceById(userId);

        List<Transference> transference = transferenceRepository.findBySourceUnitAndStatus(user.getUnit(), TransferStatus.REQUESTED);

        return transference.stream().map(
                t -> new TransfersResponse(
                t.getId(),
                t.getDestinationUnit().getName(),
                t.getRequester().getEmail(),
                t.getStatus(),
                t.getSubmissionDate(),
                t.getItems()
        )).toList();
    }
}
