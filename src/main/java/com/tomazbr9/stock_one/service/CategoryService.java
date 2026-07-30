package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.CreateCategoryRequest;
import com.tomazbr9.stock_one.entity.Category;
import com.tomazbr9.stock_one.exception.ResourceAlreadyExistsException; // Usando a exceção padronizada
import com.tomazbr9.stock_one.mapper.CategoryMapper;
import com.tomazbr9.stock_one.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public UUID createCategory(CreateCategoryRequest request) {
        log.info("Iniciando criação da categoria com nome: {}", request.name());

        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            log.warn("Falha ao criar: Já existe uma categoria com o nome {}", request.name());
            throw new ResourceAlreadyExistsException("Já existe uma categoria cadastrada com este nome.");
        }

        Category category = CategoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        log.info("Categoria criada com sucesso. ID gerado: {}", savedCategory.getId());

        return savedCategory.getId();
    }
}