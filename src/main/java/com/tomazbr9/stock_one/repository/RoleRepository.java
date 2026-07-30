package com.tomazbr9.stock_one.repository;

import com.tomazbr9.stock_one.entity.Role;
import com.tomazbr9.stock_one.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByRoleName(RoleName rolename);

}
