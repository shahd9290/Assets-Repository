package com.example.assetsystembackend.api.model;

import jakarta.persistence.*;

/**
 * Represents a backlog entity in the system, which stores messages related to assets.
 * Each backlog entry has an ID, asset ID, and message.
 */
@Entity
@Table(name = "backlog")
public class BackLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;

    @Column(name="asset_id")
    private long assetId;


    @Column(name = "message")
    private String message;

    //User in the future
    
    public long getAssetID() {
        return assetId;
    }

    public void setAsset(long asset_id) {
        this.assetId = asset_id;
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
