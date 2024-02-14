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

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    public Asset(Long id, String name, String creatorName, Date creationDate, String description, String type) {
        this.id = id;
        this.name = name;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.description = description;
        this.type = type;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorID(String creatorName) {
        this.creatorName = creatorName;
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

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}


    @Override
    public String toString(){
        String output= "{id(%d) | name(%s) | type(%s) | creation date(%s) | description(%s)}";
        return String.format(output, id, name, type, creationDate.toString(), description);
    }

}
