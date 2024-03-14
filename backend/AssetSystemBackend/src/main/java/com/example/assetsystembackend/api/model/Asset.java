package com.example.assetsystembackend.api.model;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="asset_id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="link")
    private String link;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    public Asset( String name, String creatorName, Date creationDate, String description, String type, String link) {
        this.name = name;
        this.creatorName = creatorName;
        this.creationDate = creationDate;
        this.description = description;
        this.type = type;
        this.link = link;
    }

    public Asset() { //Constructor required
    }

    public Asset(long id) { //Constructor required
        this.id = id;
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

    public String getLink() {return link;}

    public void setLink(String link) {this.link = link;}


    @Override
    public String toString(){
        String output= "{id(%d) | name(%s) | type(%s) | link(%s) | creation date(%s) | description(%s)}";
        return String.format(output, id, name, type, link, creationDate.toString(), description);
    }

}
