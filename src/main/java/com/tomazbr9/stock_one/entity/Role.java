package com.tomazbr9.stock_one.entity;

import com.tomazbr9.stock_one.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tb_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    private RoleName roleName;
}
