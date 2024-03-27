package com.example.assetsystembackend.api.model;

import jakarta.persistence.*;

/**
 * Represents a role entity in the system, which defines user roles.
 * Each role has an ID and a name.
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role(){
    }

    public Role(ERole name){
        this.name = name;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}
