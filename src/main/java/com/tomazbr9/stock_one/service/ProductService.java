package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.dto.CreateProductRequest;
import com.tomazbr9.stock_one.entity.Category;
import com.tomazbr9.stock_one.entity.Product;
import com.tomazbr9.stock_one.exception.ResourceAlreadyExistsException;
import com.tomazbr9.stock_one.exception.ResourceNotFoundException; // Usando a padrão, ou mantenha a sua se preferir
import com.tomazbr9.stock_one.mapper.ProductMapper;
import com.tomazbr9.stock_one.repository.CategoryRepository;
import com.tomazbr9.stock_one.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public UUID createProduct(CreateProductRequest request) {
        log.info("Iniciando criação do produto: {} (Código: {})", request.name(), request.code());

        if (productRepository.existsByCode(request.code())) {
            log.warn("Falha ao criar: Já existe um produto com o código {}", request.code());
            throw new ResourceAlreadyExistsException("Já existe um produto cadastrado com este código.");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> {
                    log.warn("Falha ao criar: Categoria com ID {} não encontrada.", request.categoryId());
                    return new ResourceNotFoundException("Categoria não encontrada.");
                });

        Product product = ProductMapper.toEntity(request, category);

        Product savedProduct = productRepository.save(product);

        log.info("Produto criado com sucesso. ID gerado: {}", savedProduct.getId());

        return savedProduct.getId();
    }
}