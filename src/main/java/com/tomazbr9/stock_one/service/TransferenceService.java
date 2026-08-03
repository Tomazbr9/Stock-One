package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.SolicitTransferenceRequest;
import com.tomazbr9.stock_one.dto.TransferItemRequest;
import com.tomazbr9.stock_one.dto.TransfersResponse;
import com.tomazbr9.stock_one.entity.*;
import com.tomazbr9.stock_one.enums.EquipmentStatus;
import com.tomazbr9.stock_one.enums.TransferStatus;
import com.tomazbr9.stock_one.exception.BusinessRuleException;
import com.tomazbr9.stock_one.exception.ResourceNotFoundException;
import com.tomazbr9.stock_one.mapper.TransferenceMapper;
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
    private final StockUnitRepository stockUnitRepository;

    @Transactional
    public UUID solicitTransfer(SolicitTransferenceRequest request, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Unit destinationUnit = user.getUnit();
        if (destinationUnit == null) {
            throw new BusinessRuleException("O usuário solicitante não está vinculado a nenhuma unidade de destino.");
        }

        Unit sourceUnit = unitRepository.findById(request.sourceUnitId()) // ou sourceUnitId(), dependendo do seu DTO
                .orElseThrow(() -> new ResourceNotFoundException("Unidade de origem não encontrada."));

        if (sourceUnit.getId().equals(destinationUnit.getId())) {
            throw new BusinessRuleException("A unidade de origem não pode ser igual à unidade de destino.");
        }

        Transference transference = Transference.builder()
                .sourceUnit(sourceUnit)
                .destinationUnit(destinationUnit)
                .requester(user)
                .status(TransferStatus.REQUESTED)
                .submissionDate(LocalDate.now())
                .build();

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

        List<Product> products = productIds.isEmpty() ? Collections.emptyList() : productRepository.findAllById(productIds);
        List<Equipment> equipments = equipmentIds.isEmpty() ? Collections.emptyList() : equipmentRepository.findAllById(equipmentIds);

        if (products.size() != productIds.size()) {
            throw new ResourceNotFoundException("Um ou mais produtos informados são inválidos ou não existem.");
        }
        if (equipments.size() != equipmentIds.size()) {
            throw new ResourceNotFoundException("Um ou mais equipamentos informados são inválidos ou não existem.");
        }

        Map<UUID, Product> productsMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<UUID, Equipment> equipmentMap = equipments.stream()
                .collect(Collectors.toMap(Equipment::getId, e -> e));

        List<TransferItem> transferItems = new ArrayList<>();

        for (TransferItemRequest itemDto : request.items()) {

            TransferItem.TransferItemBuilder itemBuilder = TransferItem.builder()
                    .transference(transference)
                    .quantity(itemDto.quantity());

            if (itemDto.productId() != null) {
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

    @Transactional
    public UUID dispatchTransfer(UUID transferId, UUID userId) {

        User dispatcher = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Transference transference = transferenceRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transferência não encontrada."));

        if (!transference.getStatus().equals(TransferStatus.REQUESTED)) {
            throw new BusinessRuleException("Apenas transferências com status REQUESTED podem ser enviadas.");
        }
        if (!dispatcher.getUnit().getId().equals(transference.getSourceUnit().getId())) {
            throw new BusinessRuleException("Apenas funcionários da unidade de origem podem aprovar este envio.");
        }

        List<TransferItem> productItems = transference.getItems().stream()
                .filter(item -> item.getProduct() != null)
                .toList();

        List<TransferItem> equipmentItems = transference.getItems().stream()
                .filter(item -> item.getEquipment() != null)
                .toList();

        if (!productItems.isEmpty()) {
            List<UUID> productIds = productItems.stream()
                    .map(item -> item.getProduct().getId())
                    .toList();

            List<StockUnit> stockUnits = stockUnitRepository
                    .findByUnitIdAndProductIdIn(transference.getSourceUnit().getId(), productIds);

            Map<UUID, StockUnit> stockMap = stockUnits.stream()
                    .collect(Collectors.toMap(s -> s.getProduct().getId(), s -> s));

            for (TransferItem item : productItems) {
                StockUnit stock = stockMap.get(item.getProduct().getId());

                // Valida se tem o produto e se a quantidade é suficiente
                if (stock == null || stock.getCurrentQuantity() < item.getQuantity()) {
                    throw new BusinessRuleException(
                            String.format("Estoque insuficiente na origem para o produto: %s. Requisitado: %d, Disponível: %d",
                                    item.getProduct().getName(), item.getQuantity(), (stock != null ? stock.getCurrentQuantity() : 0))
                    );
                }

                // Realiza a subtração do estoque na unidade de origem
                stock.setCurrentQuantity(stock.getCurrentQuantity() - item.getQuantity());
            }

            stockUnitRepository.saveAll(stockUnits);
        }

        if (!equipmentItems.isEmpty()) {
            List<Equipment> equipmentsToUpdate = new ArrayList<>();

            for (TransferItem item : equipmentItems) {
                Equipment equipment = item.getEquipment();

                if (!equipment.getUnit().getId().equals(transference.getSourceUnit().getId())) {
                    throw new BusinessRuleException("O equipamento " + equipment.getAssetCode() + " não se encontra na unidade de origem.");
                }

                // O equipamento sai da unidade de origem e fica em trânsito
                equipment.setStatus(EquipmentStatus.IN_TRANSIT);
                equipment.setUnit(null);

                equipmentsToUpdate.add(equipment);
            }
            equipmentRepository.saveAll(equipmentsToUpdate);
        }

        transference.setStatus(TransferStatus.IN_TRANSIT);

        Transference savedTransference = transferenceRepository.save(transference);

        return savedTransference.getId();
    }

    @Transactional
    public UUID receiptTransfer(UUID transferId, UUID userId) {

        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Transference transference = transferenceRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transferência não encontrada."));

        if (!transference.getStatus().equals(TransferStatus.IN_TRANSIT)) {
            throw new BusinessRuleException("Apenas transferências com status IN_TRANSIT podem ser recebidas.");
        }

        if (!receiver.getUnit().getId().equals(transference.getDestinationUnit().getId())) {
            throw new BusinessRuleException("Apenas funcionários da unidade de destino podem receber este envio.");
        }

        List<TransferItem> productItems = transference.getItems().stream()
                .filter(item -> item.getProduct() != null)
                .toList();

        List<TransferItem> equipmentItems = transference.getItems().stream()
                .filter(item -> item.getEquipment() != null)
                .toList();

        if (!productItems.isEmpty()) {
            List<UUID> productIds = productItems.stream()
                    .map(item -> item.getProduct().getId())
                    .toList();

            List<StockUnit> stockUnits = stockUnitRepository
                    .findByUnitIdAndProductIdIn(transference.getDestinationUnit().getId(), productIds);

            Map<UUID, StockUnit> stockUnitMap = stockUnits.stream()
                    .collect(Collectors.toMap(s -> s.getProduct().getId(), s -> s));

            List<StockUnit> stockToSave = new ArrayList<>();

            for (TransferItem item : productItems) {

                // cria o registro zerado caso o destino nunca tenha tido esse produto
                StockUnit stock = stockUnitMap.computeIfAbsent(
                        item.getProduct().getId(),
                        id -> StockUnit.builder()
                                .unit(transference.getDestinationUnit())
                                .product(item.getProduct())
                                .currentQuantity(0)
                                .minimumStock(5)
                                .build()
                );

                stock.setCurrentQuantity(stock.getCurrentQuantity() + item.getQuantity());
                stockToSave.add(stock);
            }

            stockUnitRepository.saveAll(stockToSave);
        }

        if (!equipmentItems.isEmpty()) {
            List<Equipment> equipmentsToUpdate = new ArrayList<>();

            for (TransferItem item : equipmentItems) {
                Equipment equipment = item.getEquipment();

                // O equipamento chega na nova unidade
                equipment.setUnit(transference.getDestinationUnit());
                equipment.setStatus(EquipmentStatus.IN_USE);

                equipmentsToUpdate.add(equipment);
            }

            equipmentRepository.saveAll(equipmentsToUpdate);
        }

        transference.setStatus(TransferStatus.RECEIVED);
        transference.setReceiver(receiver);
        transference.setReceiptDate(LocalDate.now());

        Transference savedTransference = transferenceRepository.save(transference);

        return savedTransference.getId();
    }

    public List<TransfersResponse> getTransfersPending(UUID userId){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if(user.getUnit() == null){
            throw new BusinessRuleException("O usuário não possui uma unidade vinculada");
        }

        List<Transference> transference = transferenceRepository.findBySourceUnitAndStatus(user.getUnit(), TransferStatus.REQUESTED);

        return TransferenceMapper.toDtoList(transference);
    }
}
