package com.tomazbr9.stock_one.service;

import com.tomazbr9.stock_one.config.SecurityConfiguration;
import com.tomazbr9.stock_one.dto.CreateUserRequest;
import com.tomazbr9.stock_one.dto.LoginUserRequest;
import com.tomazbr9.stock_one.dto.RecoveryJwtTokenResponse;
import com.tomazbr9.stock_one.entity.Role;
import com.tomazbr9.stock_one.entity.Unit;
import com.tomazbr9.stock_one.entity.User;
import com.tomazbr9.stock_one.exception.UnitNotFoundException;
import com.tomazbr9.stock_one.repository.RoleRepository;
import com.tomazbr9.stock_one.repository.UnitRepository;
import com.tomazbr9.stock_one.repository.UserRepository;
import com.tomazbr9.stock_one.security.JwtTokenService;
import com.tomazbr9.stock_one.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;

    private final SecurityConfiguration securityConfiguration;

    private final RoleRepository roleRepository;

    private final UnitRepository unitRepository;

    public RecoveryJwtTokenResponse authenticateUser(LoginUserRequest request) {

        log.info("Solicitação de login para usuário: {}", request.email());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        log.info("Usuário {} logado com sucesso", request.email());

        return new RecoveryJwtTokenResponse(jwtTokenService.generateToken(userDetails));
    }

    @Transactional
    public UUID createUser(CreateUserRequest request) {

        log.info("Solicitação para criar novo usuário. Email: {}, Unidade Id: {}", request.email(), request.unitId());

        Role roleEntity = roleRepository.findByRoleName(request.role()).orElseThrow(() -> new RuntimeException("Role não encontrada"));

        Unit unitEntity = unitRepository.findById(request.unitId()).orElseThrow(() -> new UnitNotFoundException("Unidade não encontrada"));

        User newUser = User.builder()
                .matriculation(request.matriculation())
                .email(request.email())
                .password(securityConfiguration.passwordEncoder().encode(request.password()))
                .unit(unitEntity)
                .roles(Set.of(roleEntity))
                .active(true)
                .build();

        User user = userRepository.save(newUser);

        log.info("Usuário criado com sucesso: {}", user.getEmail());

        return user.getId();
    }
}