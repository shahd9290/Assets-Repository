package com.example.assetsystembackend.api.model;

import jakarta.persistence.*;


@Entity
@Table(name = "backlog")
public class BackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="asset_id", referencedColumnName = "asset_id", nullable = false)
    private Asset asset;


    @Column(name = "message")
    private String message;

    //User in the future
    
    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
