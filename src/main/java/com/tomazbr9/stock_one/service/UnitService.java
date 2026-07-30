package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.CreateUnitRequest;
import com.tomazbr9.stock_one.entity.Unit;
import com.tomazbr9.stock_one.exception.ResourceAlreadyExistsException;
import com.tomazbr9.stock_one.mapper.UnitMapper;
import com.tomazbr9.stock_one.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    @Transactional
    public UUID createUnit(CreateUnitRequest request) {
        log.info("Iniciando criação da unidade com nome: {}", request.name());

        if (unitRepository.existsByNameIgnoreCase(request.name())) {
            log.warn("Falha ao criar: Já existe uma unidade com o nome {}", request.name());
            throw new ResourceAlreadyExistsException("Já existe uma unidade cadastrada com este nome.");
        }

        if (request.isCentral() && unitRepository.existsByIsCentralTrue()) {
            log.warn("Falha ao criar: Tentativa de criar um segundo Estoque Central");
            throw new ResourceAlreadyExistsException("Já existe um Estoque Central cadastrado no sistema.");
        }

        Unit unit = UnitMapper.toEntity(request);

        Unit savedUnit = unitRepository.save(unit);

        log.info("Unidade criada com sucesso. ID gerado: {}", savedUnit.getId());

        return savedUnit.getId();
    }
}