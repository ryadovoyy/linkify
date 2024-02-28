package com.ryadovoy.linkify.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "role")
@Getter
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName name;
}
