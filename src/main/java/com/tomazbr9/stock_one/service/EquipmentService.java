package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.CreateEquipmentRequest;
import com.tomazbr9.stock_one.entity.Category;
import com.tomazbr9.stock_one.entity.Equipment;
import com.tomazbr9.stock_one.entity.Unit;
import com.tomazbr9.stock_one.entity.User;
import com.tomazbr9.stock_one.enums.EquipmentStatus;
import com.tomazbr9.stock_one.exception.ResourceNotFoundException;
import com.tomazbr9.stock_one.repository.CategoryRepository;
import com.tomazbr9.stock_one.repository.EquipmentRepository;
import com.tomazbr9.stock_one.repository.UnitRepository;
import com.tomazbr9.stock_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final UnitRepository unitRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional
    public UUID createEquipment(CreateEquipmentRequest request, UUID userId){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Unit centralUnit = unitRepository.findById(user.getUnit().getId()).orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));

        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Equipment equipment = Equipment.builder()
                .assetCode(request.assetCode())
                .name(request.name())
                .category(category)
                .serialNumber(request.serialNumber())
                .brand(request.brand())
                .acquisitionDate(request.acquisitionDate())
                .status(EquipmentStatus.AVAILABLE)
                .unit(centralUnit)
                .build();

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return savedEquipment.getId();

    }
}
