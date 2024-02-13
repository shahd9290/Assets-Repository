package com.example.assetsystembackend.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @Column(name="asset_id")
    private final Long id;

    @Column(name="name")
    private String name;

    @Column(name = "creator_id")
    private Long creatorID;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "description")
    private String description;

    public Asset(Long id, String name, Long creatorID, Date creationDate, String description) {
        this.id = id;
        this.name = name;
        this.creatorID = creatorID;
        this.creationDate = creationDate;
        this.description = description;
    }

    public Asset() { //Constructor required
        id = -1L; //required due to final id
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Long creatorID) {
        this.creatorID = creatorID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        String output= "{id(%d) | name(%s) | creation date(%s) | description(%s)}";
        return String.format(output, id, name, creationDate.toString(), description);
    }

}
